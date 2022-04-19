package socialCreditBot

import com.sun.net.httpserver.{HttpExchange, HttpHandler, HttpServer}
import socialCreditBot.bot.SocialCreditBot
import socialCreditBot.repository.UserRatingRepository

import java.net.InetSocketAddress
import scala.concurrent.duration.Duration
import scala.concurrent.Await


object SocialCreditBotApp extends App {
  val port: Int = sys.env("PORT").toInt
  val webhookUrl = sys.env("WEBHOOK_URL")
  val db = new UserRatingRepository()
  val bot = new SocialCreditBot(sys.env("BOT_TOKEN"), port, webhookUrl, db)
  val eol = bot.run()
  println("Bot started")
//  val server = HttpServer.create(new InetSocketAddress(port), 0)
//  server.createContext("/", new HttpHandler() {
//    override def handle(exchange: HttpExchange): Unit = {
//      val response = "Ack!"
//      exchange.sendResponseHeaders(200, response.length())
//      val os = exchange.getResponseBody
//      os.write(response.getBytes)
//      os.close()
//    }
//  })
//  server.setExecutor(null)
//  server.start()
  // Wait for the bot end-of-life
  Await.result(eol, Duration.Inf)
}
