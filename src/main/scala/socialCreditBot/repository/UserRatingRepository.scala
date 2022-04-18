package socialCreditBot.repository

import slick.jdbc.JdbcBackend.Database
import slick.lifted.Tag
import slick.jdbc.PostgresProfile.api._
import socialCreditBot.model.UserSocialCredit

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class UserRatingRepository {

  val db = Database.forConfig("pg")

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
