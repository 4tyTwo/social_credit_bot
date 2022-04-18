package socialCreditBot.repository

import slick.jdbc.JdbcBackend.Database
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._
import socialCreditBot.model.UserSocialCredit

import java.net.URI
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.Properties

class UserRatingRepository {

  val dbUri = new URI(sys.env("DATABASE_URL"))
  val username: String = dbUri.getUserInfo.split(":")(0)
  val password: String = dbUri.getUserInfo.split(":")(1)
  val dbUrl: String = "jdbc:postgresql://" + dbUri.getHost + ':' + dbUri.getPort + dbUri.getPath + "?sslmode=require"
  val db = Database.forURL(dbUrl, user= username, password = password, driver = "org.postgresql.Driver")

  private val socialCredits = TableQuery[UserSocialCredits]

  def getRating(userId: Long, chatId: Long): Future[Option[UserSocialCredit]] = {
    db.run(socialCredits.filter {r => r.userId === userId && r.chatId === chatId }.result.headOption)
  }

  def changeRating(userId: Long, chatId: Long, changeAmount: Int): Future[Int] =
    getRating(userId, chatId) flatMap {
      case Some(value) => {
        println("Found rating for " + userId + " in " + chatId)
        val q = for { credit <- socialCredits if credit.userId === userId && credit.chatId === chatId} yield credit.rating
        db.run(q.update(value.rating + changeAmount))
      }
      case None => {
        println("ChatId: " + chatId + " userId: " + userId + " had no record")
        db.run(socialCredits += UserSocialCredit(userId, chatId, changeAmount))
        getRating(userId, chatId).map {_.get.rating}
      }
    }

  class UserSocialCredits(tag: Tag) extends Table[UserSocialCredit](tag, "social_credits") {
    def userId: Rep[Long] = column[Long]("user_id")
    def chatId: Rep[Long] = column[Long]("chat_id")
    def rating: Rep[Int] = column[Int]("rating")

    def * = (userId, chatId, rating) <> (UserSocialCredit.tupled, UserSocialCredit.unapply)
  }
}
