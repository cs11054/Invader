package main.scala.window

import javax.swing.JPanel
import java.awt.Graphics2D
import java.awt.Graphics
import java.awt.Color
import main.scala.exec.Main
import main.scala.controller.GameCtr
import main.scala.character._
import java.awt.event.KeyEvent
import java.awt.event.KeyAdapter
import main.scala.character.Character
import java.awt.AlphaComposite
import main.scala.etc.Scene
import java.awt.Font
import java.awt.RenderingHints

// ゲームのModel
class GamePanel(ctr: GameCtr) extends JPanel {

  override def paint(grp: Graphics) {
    val g = grp.asInstanceOf[Graphics2D]
    g.setColor(Color.BLACK)
    g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT)
    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // 今回は描画する量が少ないからシーンによって描画する内容を分けることにした(つまりめんどくさかったから)
    ctr.scene match {

      /* *************メニュー部分************* */
      case Scene.MENU =>
        Character.imgMap.values.foreach(g.drawImage(_, 0, 0, this)) // 強引に画像ロード
        g.fillRect(0, 0, Main.WIDTH, Main.HEIGHT)
        g.setColor(Color.BLUE)
        g.setFont(new Font("メイリオ", Font.BOLD, 32))
        g.drawString("Push Enter key to Start", 100, Main.HEIGHT / 2)

      /* *************ゲーム部分************* */
      case Scene.GAME =>
        // 死んでいる敵の血
        ctr.enemys.filter(!_.life).foreach(e => g.drawImage(Character.imgMap(s"blad${e.bladType}"), e.x, e.y, this))
        // 自分の弾
        g.setColor(Color.GRAY)
        ctr.player.bullets.filter(_.enable).foreach(b => g.fillRect(b.x, b.y, b.w, b.h))
        // 敵の弾
        g.setColor(Color.RED)
        ctr.enemys.foreach(_.bullets.filter(_.enable).foreach { b =>
          g.fillRect(b.x, b.y, b.w, b.h)
          g.fillRect(b.x - 3, b.y + 2, b.w + 6, 2)
        })
        // 壁、耐久度により薄くなっていく
        ctr.walls.filter(_.life).foreach { w =>
          g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, w.lifeCount / Wall.LIFE));
          g.drawImage(w.icon, w.x, w.y, this)
        }
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1F));
        // 自分
        g.drawImage(ctr.player.icon, ctr.player.x, ctr.player.y, this)
        // 敵
        ctr.enemys.filter(_.life).foreach(e => g.drawImage(e.icon, e.x, e.y, this))
        // 壊れた壁
        ctr.walls.filter(!_.life).foreach(w => g.drawImage(Wall.scrap, (w.x + w.w / 2 - Wall.sw / 2), (w.y + w.h / 2 - Wall.sh / 2), this))

      /* *************ゲーム終了後部分************* */
      case Scene.FINISH =>
        val msg = if (ctr.player.life) s"抵抗する人々を${ctr.clearTime}秒で撲滅完了！" else "どんまい＾＾；"
        g.setColor(Color.BLUE)
        g.setFont(new Font("メイリオ", Font.BOLD, 32))
        g.drawString(msg, 100, Main.HEIGHT / 2 - 100)
        g.drawString("Push Enter key to Restart", 100, Main.HEIGHT / 2)

    }
  }

}