
import com.badlogic.gdx.backends.headless.HeadlessApplication

object ServerLauncher {

  def main(args: Array[String]): Unit = {
    new HeadlessApplication(new AsteroidsSeverGame)
  }


}
