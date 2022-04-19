package socialCreditBot

import socialCreditBot.bot.SocialCreditBot
import socialCreditBot.repository.UserRatingRepository

import scala.concurrent.duration.Duration
import scala.concurrent.Await


object SocialCreditBotApp extends App {
  val port: Int = sys.env("PORT").toInt
  val webhookUrl = sys.env("WEBHOOK_URL")
  val db = new UserRatingRepository()
  val bot = new SocialCreditBot(sys.env("BOT_TOKEN"), port, webhookUrl, db)
  val eol = bot.run()
  println("Bot started")
  // Wait for the bot end-of-life
  Await.result(eol, Duration.Inf)
}
