package socialCreditBot

import socialCreditBot.bot.SocialCreditBot

import scala.concurrent.Await
import scala.concurrent.duration.Duration

object SocialCreditBotApp extends App {
  val bot = new SocialCreditBot("")
  val eol = bot.run()
  println("Press [ENTER] to shutdown the bot, it may take a few seconds...")
  scala.io.StdIn.readLine()
  bot.shutdown()
  // Wait for the bot end-of-life
  Await.result(eol, Duration.Inf)
}
