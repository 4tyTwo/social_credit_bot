package socialCreditBot.bot
import com.bot4s.telegram.api.RequestHandler
import com.bot4s.telegram.api.declarative.Commands
import com.bot4s.telegram.clients.FutureSttpClient
import com.bot4s.telegram.future.{Polling, TelegramBot}
import com.bot4s.telegram.models.Message
import socialCreditBot.UserRating
import sttp.client3.SttpBackend
import sttp.client3.httpclient.HttpClientFutureBackend

import scala.concurrent.Future

class SocialCreditBot(val token: String) extends TelegramBot with Polling with Commands[Future] {

  implicit val backend: SttpBackend[Future, Any] = HttpClientFutureBackend()
  override val client: RequestHandler[Future] = new FutureSttpClient(token)

  override def receiveMessage(msg: Message): Future[Unit] = {
    msg.sticker.foreach {
      sticker => {
        sticker.fileUniqueId match {
          case "AgADAgADf3BGHA" => msg.replyToMessage.foreach { m => m.from.foreach { u => UserRating.addRating(u.id) } }
          case "AgADAwADf3BGHA" => msg.replyToMessage.foreach { m => m.from.foreach { u => UserRating.subtractRating(u.id) } }
          case _ => ()
        }
      }
    }
    Future{}
  }

  onCommand("mystat") { implicit  msg =>
    println(UserRating.userRating)
    msg.from.foreach {u => reply(UserRating.userRating.getOrElse(u.id, 0).toString)}
    unit
  }

}