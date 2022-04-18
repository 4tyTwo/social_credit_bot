package socialCreditBot

import socialCreditBot.bot.SocialCreditBot
import socialCreditBot.repository.UserRatingRepository

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


object SocialCreditBotApp extends App {
  val db = new UserRatingRepository()
  val bot = new SocialCreditBot(sys.env("BOT_TOKEN"), db)
  val eol = bot.run()
  println("Press [ENTER] to shutdown the bot, it may take a few seconds...")
  scala.io.StdIn.readLine()
  bot.shutdown()
  db.db.close()
  // Wait for the bot end-of-life
  Await.result(eol, Duration.Inf)
}
