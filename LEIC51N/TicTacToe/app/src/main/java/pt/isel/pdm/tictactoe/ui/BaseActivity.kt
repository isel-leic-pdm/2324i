package pt.isel.pdm.tictactoe.ui

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.activity.ComponentActivity
import pt.isel.pdm.tictactoe.DependencyContainer
import pt.isel.pdm.tictactoe.ui.home.HomeActivity

abstract class BaseActivity : ComponentActivity()
{
    protected val dependencyContainer by lazy { (application as DependencyContainer) }
    protected val activityTag: String = this.javaClass.simpleName

    protected inline fun <reified T> navigate(
        noinline apply: ((Intent) -> Unit)? = null
    ) {

        val intent = Intent(this, T::class.java)

        if (apply != null)
            apply(intent)

        this.startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        Log.d(activityTag, "onCreate")
        super.onCreate(savedInstanceState, persistentState)
    }

    override fun onStart() {
        Log.d(activityTag, "onStart")
        super.onStart()
    }

    override fun onStop() {
        Log.d(activityTag, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(activityTag, "onDestroy")
        super.onDestroy()
    }

}