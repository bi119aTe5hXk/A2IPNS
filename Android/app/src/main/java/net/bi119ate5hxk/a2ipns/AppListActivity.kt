package net.bi119ate5hxk.a2ipns

import android.content.Context
import android.content.pm.PackageInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_app_list.*

class AppListActivity : AppCompatActivity() {
    private lateinit var installedPackages: MutableList<PackageInfo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_app_list)

        setSupportActionBar(findViewById(R.id.actionToolbar))

        this.title = getString(R.string.pref_title_action_bar_select_apps)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        installedPackages = packageManager.getInstalledPackages(0)
        installedPackages.sortWith(compareBy { info ->
            info.applicationInfo.loadLabel(packageManager).toString()
        })

        appListRecyclerView.apply {
            setHasFixedSize(true)

            layoutManager = LinearLayoutManager(this@AppListActivity)
            adapter = AppListAdapter(installedPackages)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_bar_app_list, menu)

        val menuItem = menu?.findItem(R.id.search)
        val searchView = menuItem?.actionView as SearchView

        menuItem.setOnMenuItemClickListener {
            searchView.requestFocus()
        }

        searchView.setIconifiedByDefault(false)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    val packageInfo = installedPackages.singleOrNull { info ->
                        info.applicationInfo.loadLabel(packageManager).contains(
                            newText,
                            true
                        ) || info.packageName.contains(newText, true)
                    }

                    if (packageInfo != null) {
                        appListRecyclerView.scrollToPosition(installedPackages.indexOf(packageInfo))
                    }
                }

                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    class AppListAdapter(private val appList: List<PackageInfo>) :
        RecyclerView.Adapter<AppListAdapter.AppListViewHolder>() {
        class AppListViewHolder(private val view: ConstraintLayout) : RecyclerView.ViewHolder(view)

        private lateinit var context: Context

        override fun getItemCount(): Int = appList.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppListViewHolder {
            context = parent.context

            val itemView =
                LayoutInflater.from(parent.context).inflate(
                    R.layout.view_app_item,
                    parent,
                    false
                ) as ConstraintLayout

            return AppListViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: AppListViewHolder, position: Int) {
            holder.itemView.findViewById<ImageView>(R.id.appIconImageView)
                .setImageDrawable(appList[position].applicationInfo.loadIcon(context.packageManager))
            holder.itemView.findViewById<TextView>(R.id.appNameTextView).text =
                appList[position].applicationInfo.loadLabel(context.packageManager)
            holder.itemView.findViewById<TextView>(R.id.appPackageNameTextView).text =
                appList[position].packageName

            val appSelectedCheckBox = holder.itemView.findViewById<CheckBox>(R.id.appSelectedCheckBox)
            val selectedAppList =
                AppHelper.Settings.getStringSet(context.getString(R.string.pref_key_selected_apps), null)

            if (selectedAppList != null) {
                appSelectedCheckBox.isChecked = selectedAppList.contains(appList[position].packageName)
                appSelectedCheckBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked && !selectedAppList.contains(appList[position].packageName)) {
                        selectedAppList.add(appList[position].packageName)
                    } else if (!isChecked) {
                        selectedAppList.remove(appList[position].packageName)
                    }

                    AppHelper.Settings.edit()
                        .putStringSet(context.getString(R.string.pref_key_selected_apps), selectedAppList)
                        .apply()
                }
            }
        }
    }
}