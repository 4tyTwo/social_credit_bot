package socialCreditBot.bot

import com.bot4s.telegram.api.{AkkaTelegramBot, RequestHandler, Webhook}
import com.bot4s.telegram.api.declarative.Commands
import com.bot4s.telegram.clients.AkkaHttpClient
import com.bot4s.telegram.models.Message
import socialCreditBot.repository.UserRatingRepository

import scala.concurrent.Future

class SocialCreditBot(val token: String, val port: Int, val webhookUrl: String, val db: UserRatingRepository) extends AkkaTelegramBot with Webhook with Commands[Future] {

  override val client: RequestHandler[Future] = new AkkaHttpClient(token)

  override def receiveMessage(msg: Message): Future[Unit] = {
    Future {
      val chatId = msg.chat.id
      msg.sticker.foreach {
        sticker => {
          val ratingChange = sticker.fileUniqueId match {
            case "AgADAgADf3BGHA" => 20
            case "AgADAwADf3BGHA" => -20
          }
          val fromId = msg.from.map { _.id}
          val toId = msg.replyToMessage.flatMap { _.from.map { u => u.id } }
          if (fromId.isDefined && toId.isDefined) {
            if (fromId.get != toId.get) {
              db.changeRating(toId.get, chatId, ratingChange)
            }
          }
        }
      }
    }
  }

  onCommand("mystat") { implicit  msg =>
    Future {
      val userId = msg.from.flatMap {u => Some(u.id)}.getOrElse(0L)
      getUserRating(userId, msg.chat.id) foreach { r => reply(r.toString, replyToMessageId = Some(msg.messageId))}
    }
  }

  onCommand("stat") { implicit  msg =>
    Future {
      val forUser = msg.replyToMessage.flatMap { _.from.map {_.id}}
      forUser.foreach
        { userId =>
            getUserRating(userId, msg.chat.id) foreach { r => reply(r.toString, replyToMessageId = Some(msg.messageId))}
        }
    }
  }

  def getUserRating(userId: Long, chatId: Long): Future[Int] =
    db.getRating(userId, chatId) map {
      case Some(value) => value.rating
      case None => 0
    }

}