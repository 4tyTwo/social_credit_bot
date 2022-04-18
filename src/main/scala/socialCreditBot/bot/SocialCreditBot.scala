package socialCreditBot.bot

import com.bot4s.telegram.api.RequestHandler
import com.bot4s.telegram.api.declarative.Commands
import com.bot4s.telegram.clients.FutureSttpClient
import com.bot4s.telegram.future.{Polling, TelegramBot}
import com.bot4s.telegram.models.Message
import socialCreditBot.repository.UserRatingRepository
import sttp.client3.SttpBackend
import sttp.client3.httpclient.HttpClientFutureBackend

import scala.concurrent.Future

class SocialCreditBot(val token: String, val db: UserRatingRepository) extends TelegramBot with Polling with Commands[Future] {

  implicit val backend: SttpBackend[Future, Any] = HttpClientFutureBackend()
  override val client: RequestHandler[Future] = new FutureSttpClient(token)

  override def receiveMessage(msg: Message): Future[Unit] = {
    Future {
      val chatId = msg.chat.id
      println("chatID: " + chatId)
      msg.sticker.foreach {
        sticker => {
          println("msg is a sticker")
          sticker.fileUniqueId match {
            case "AgADAgADf3BGHA" => msg.replyToMessage.foreach { m => m.from.foreach { u => db.changeRating(u.id, chatId, 20) } }
            case "AgADAwADf3BGHA" => msg.replyToMessage.foreach { m => m.from.foreach { u => db.changeRating(u.id, chatId, -20) } }
            case _ => println("unknown sticker"); ()
          }
        }
      }
    }
  }

  onCommand("mystat") { implicit  msg =>
    Future {
      val userId = msg.from.flatMap {u => Some(u.id)}.getOrElse(0L)
      getUserRating(userId, msg.chat.id) foreach { r => reply(r.toString)}
      unit
    }
  }

  def getUserRating(userId: Long, chatId: Long): Future[Int] =
    db.getRating(userId, chatId) map {
      case Some(value) => value.rating
      case None => 0
    }
}