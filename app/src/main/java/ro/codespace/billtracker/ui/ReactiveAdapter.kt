package ro.codespace.billtracker.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

class ReactiveAdapter<T>(val layout: Int, flowable: Flowable<List<T>>, val binder: (itemView: View, item: T) -> Unit) : RecyclerView.Adapter<ReactiveAdapter<T>.ViewHolder>(), Disposable {
    private var dataset = listOf<T>()
    private val disposable: Disposable

    init {
        disposable = flowable.observeOn(AndroidSchedulers.mainThread()).subscribe {
            dataset = it
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int) = ViewHolder(LayoutInflater.from(viewGroup.context).inflate(layout, viewGroup, false))

    override fun getItemCount() = dataset.size

    override fun onBindViewHolder(viewHolder: ReactiveAdapter<T>.ViewHolder, position: Int) = viewHolder.bind(dataset[position])

    override fun isDisposed() = disposable.isDisposed

    override fun dispose() = disposable.dispose()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(t: T) = binder(itemView, t)
    }
}