package socialCreditBot

object UserRating {
  var userRating: Map[Long, Int] = Map()

  val step: Int = 20

  def addRating(user: Long): Unit = userRating = userRating.updatedWith(user) {
    case Some(rating) => Some(rating + step)
    case None => Some(step)
  }

  def subtractRating(user: Long): Unit =
    userRating = userRating.updatedWith(user) {
      case Some(rating) => Some(rating - step)
      case None => Some(-step)
    }
}
