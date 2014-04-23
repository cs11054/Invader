package main.scala.model

import javax.swing.JPanel
import main.scala.exec.Main
import main.scala.controller.GameCtr
import javax.swing.SwingUtilities
import main.scala.etc.Scene
import main.scala.etc.Keys
import scala.collection.mutable.Buffer
import java.awt.image.BufferedImage
import scala.util.Random
import main.scala.character._

// ゲームのModel
case class Game(ctr: GameCtr) {

  val ENE_COL = 4
  val ENE_RAW = 10
  val ENE_START_X = 15
  val ENE_SPACE_HEIGHT = 40
  val ENE_SPACE_WIDTH = 50

  val WALL_RAW = 4
  val WALL_HEIGHT = Main.HEIGHT - 200
  val WALL_SPACE_WIDTH = Main.WIDTH / (WALL_RAW + 1) - 50 / 2

  val player = new Player(Main.WIDTH / 2, Main.HEIGHT / 1.2 toInt, "player")
  val enemys = for (x <- 0 to ENE_RAW; y <- 0 to ENE_COL)
    yield new Enemy(x * ENE_SPACE_WIDTH + ENE_START_X, y * ENE_SPACE_HEIGHT, s"enemy${y % 5}")
  val walls = for (x <- 1 to WALL_RAW) yield new Wall(x * WALL_SPACE_WIDTH, WALL_HEIGHT, "wall")

  val rnd = new Random()

  val sleeper = makeSleeper(update) // なんとなくクロージャ
  var gaming = true
  def start() {
    while (gaming) {
      sleeper()
    }
  }

  var startTime: Long = _ // クリアまでの時間測定用
  var endTime: Long = _ // クリアまでの時間測定用
  var rain: Boolean = false // 壁が全部壊れた時に1回だけ雨を降らせるためのフラグ

  // 1フレームごとに更新する内容
  private def update() {
    // シーンによって更新する内容を分ける
    ctr.scene match {

      /* *************メニュー************* */
      case Scene.MENU =>
        if (ctr.keys.toSet.contains(Keys.Enter)) {
          ctr.scene = Scene.GAME
          startTime = System.currentTimeMillis();
        }

      /* *************ゲーム************* */
      case Scene.GAME =>
        player.move(ctr.keys.toSet)
        player.shot(ctr.keys.toSet)
        // プレイヤーの弾の当たり判定、enemysとwallsを結合or新たなSeqにして回せばシンプルになるけど、無駄なインスタンスを生成しそうだからやめた
        for (b <- player.bullets.filter(_.enable)) {
          b.move()
          for (obj <- enemys.filter(_.life) if b.isHit(obj)) { obj.hit(); b.enable = false }
          for (obj <- walls.filter(_.life) if b.isHit(obj)) { obj.hit(); b.enable = false }
        }
        // 敵関連、死んだ敵も弾は生きているのが気持ち悪い
        enemys.foreach { e =>
          if (e.life) {
            e.move()
            e.shot(enemys.filter(_.life).size)
            if ((e.y + e.h) > player.y) player.hit()
          }
          for (b <- e.bullets.filter(_.enable)) {
            b.move()
            for (obj <- walls.filter(_.life) if b.isHit(obj)) { obj.hit(); b.enable = false }
            if (b.isHit(player)) { player.hit(); b.enable = false }
          }
        }
        // 壁がなくなったときの雨
        if (!rain && walls.filter(_.life).size == 0) {
          enemys.filter(_.life).foreach(_.shot())
          rain = true
        }
        // ゲーム終了判定
        if (!player.life || enemys.forall(!_.life)) {
          endTime = System.currentTimeMillis()
          ctr.scene = Scene.FINISH
        }

      /* *************終了************* */
      case Scene.FINISH =>
        if (ctr.keys.toSet.contains(Keys.Enter)) gaming = false

    }
    ctr.paint

  }

  // 宣言されたFPSで渡された関数を呼び続ける
  private def makeSleeper(f: () => Unit) = {
    val FPS = 60
    val idealSleep = (1000 << 16) / FPS
    var error = 0L
    var oldTime = 0L
    var newTime = System.currentTimeMillis() << 16;
    () => {
      oldTime = newTime;
      SwingUtilities.invokeLater(new Runnable() {
        override def run() {
          f()
        }
      })
      newTime = System.currentTimeMillis() << 16
      var sleepTime = idealSleep - (newTime - oldTime) - error
      if (sleepTime < 0x20000) sleepTime = 0x20000
      oldTime = newTime
      Thread.sleep(sleepTime >> 16)
      newTime = System.currentTimeMillis() << 16
      error = newTime - oldTime - sleepTime;
    }
  }
}