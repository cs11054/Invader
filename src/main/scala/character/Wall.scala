package main.scala.character

import scala.util.Random

// 壁、壁もキャラクター
class Wall(xx: Int, yy: Int, name: String) extends Character(xx, yy, name) {
  var lifeCount = Wall.LIFE	// 壁のライフ
  val bullets = Seq[RectBullet]()
  val bladType = (new Random)

  override def hit() = {
    lifeCount -= 1
    if (lifeCount <= Wall.MIN) life = false
  }

}

object Wall {
  val MIN = 5F	// 壁の限界、めんどくさいので描画部分に依存した設計
  val LIFE = 10 + MIN
  val scrap = Character.imgMap("scrap")
  val sw = scrap.getWidth()
  val sh = scrap.getHeight()
}