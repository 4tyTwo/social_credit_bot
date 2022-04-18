package socialCreditBot

import socialCreditBot.bot.SocialCreditBot
import socialCreditBot.repository.UserRatingRepository

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


object SocialCreditBotApp extends App {
  val db = new UserRatingRepository()
  val bot = new SocialCreditBot(sys.env("BOT_TOKEN"), db)
  val eol = bot.run()
  // Wait for the bot end-of-life
  Await.result(eol, Duration.Inf)
}
