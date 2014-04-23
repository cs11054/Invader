package main.scala.character

import main.scala.etc.Keys
import main.scala.exec.Main

// プレイヤーが操作するキャラ
class Player(xx: Int, yy: Int, name: String) extends Character(xx, yy, name) {
  x -= w / 2
  override val bullets = for (x <- 0 until Player.MAX_BULLET)
    yield (new RectBullet(Player.BULLET_WIDTH, Player.BULLET_HEIGHT, Player.BULLET_SPEED))

  def move(keys: Set[Keys]) {
    if (keys.contains(Keys.Left)) x -= Player.MOVE_DIST
    if (keys.contains(Keys.Right)) x += Player.MOVE_DIST
    if (x < 0) x = 0
    else if ((x + w) > (Main.ADJ_W)) x = Main.ADJ_W - w
  }

  var coolTime = Player.BULLET_COOLTIME	// 一度弾を撃ったらクールタイムを設定する
  def shot(keys: Set[Keys]) {
    coolTime -= 1
    if (keys.contains(Keys.Space) && coolTime < 0) {
      shot()
      coolTime = Player.BULLET_COOLTIME
    }
  }

}

object Player {
  val MOVE_DIST = 3
  val MAX_BULLET = 3
  val BULLET_SPEED = -3
  val BULLET_COOLTIME = 20
  val BULLET_HEIGHT = 20
  val BULLET_WIDTH = 4
}