package main.scala.character

import main.scala.exec.Main

// 長方形の弾
class RectBullet(val w: Int, val h: Int, val a: Int) extends AbstractBullet {

  override def move() {
    y += a
    if ((y - h) > Main.HEIGHT || y < 0) enable = false
  }

  override def isHit(trg: Character): Boolean = {
    val (x0, y0, x1, y1) = (trg.x, trg.y, trg.x + trg.w, trg.y + trg.h)
    val (x2, y2, x3, y3) = (x, y, x + w, y + h)
    x0 < x3 && x2 < x1 && y0 < y3 && y2 < y1
  }

}