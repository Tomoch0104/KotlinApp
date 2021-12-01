package io.kenji.sample.mastodonclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null){
            // TootListFragmentをインスタンス化
            val fragment = TootListFragment()
            supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    fragment,
                    TootListFragment.TAG // FragmentManagerに追加する際に設定するTAGを変更
                )
                .commit()
        }
    }
}