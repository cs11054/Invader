package main.scala.window

import javax.swing.JFrame
import main.scala.controller.GameCtr
import java.awt.event.KeyAdapter
import java.awt.event.KeyEvent

// フレーム、無名クラスで十分だったか？
class MainFrame(ctr: GameCtr, title: String) extends JFrame(title) {
  val gp = new GamePanel(ctr)
  this.add(gp)
  setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
  setResizable(false)
  addKeyListener(new KeyAdapter() {
    override def keyPressed(e: KeyEvent) { ctr.keyPressed(e) }
    override def keyReleased(e: KeyEvent) { ctr.keyReleased(e) }
  })

}