package main.scala.character
import scala.util.Random
import main.scala.character.Enemy.Direct

// 敵
class Enemy(xx: Int, yy: Int, name: String) extends Character(xx, yy, name) {
  override val bullets = for (x <- 0 to Enemy.MAX_BULLET)
    yield (new RectBullet(Enemy.BULLET_WIDTH, Enemy.BULLET_HEIGHT, Enemy.BULLET_SPEED))
  val rnd = new Random

  var direct: Direct = Enemy.Right
  var coolTime = Enemy.MOVE_COOLTIME
  var moveCount = 0
  val bladType = rnd.nextInt(3) + 1

  // 一定時間ごとに横に移動する、一定回数移動したら下に移動する
  def move() {
    coolTime -= 1
    if (coolTime > 0) return

    x = direct match {
      case Enemy.Right => x + Enemy.MOVE_X
      case Enemy.Left => x - Enemy.MOVE_X
    }
    coolTime = Enemy.MOVE_COOLTIME

    moveCount += 1
    if (moveCount > Enemy.MOVE_N) {
      moveCount = 0
      y += Enemy.MOVE_Y
      direct = direct match {
        case Enemy.Right => Enemy.Left
        case Enemy.Left => Enemy.Right
      }
    }
  }

  def shot(size: Int) {
    if (rnd.nextInt(60) < 1 && rnd.nextInt(size) < 5)
      shot()
  }

  override def hit() = {
    life = false
    shot()
  }

}

object Enemy {
  val MOVE_X = 20
  val MOVE_Y = 35
  val MOVE_N = 10
  val MOVE_COOLTIME = 60

  val BULLET_HEIGHT = 12
  val BULLET_WIDTH = 3
  val BULLET_SPEED = 4
  val MAX_BULLET = 10

  sealed abstract class Direct
  case object Right extends Direct
  case object Left extends Direct

}