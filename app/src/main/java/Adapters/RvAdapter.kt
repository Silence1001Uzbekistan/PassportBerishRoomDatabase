package Adapters

import Models.Citizens
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import uz.artikov.passportberishroomdatabase.databinding.ItemRvBinding

class RvAdapter(val list: List<Citizens>,var rvOnClick: RvOnClick):RecyclerView.Adapter<RvAdapter.Vh>() {

    inner class Vh(var itemRvBinding: ItemRvBinding):RecyclerView.ViewHolder(itemRvBinding.root){

        fun onBind(citizens: Citizens,position: Int){

            itemRvBinding.txtItemRvName.text = "${citizens.name}\t ${citizens.lastName}"
            itemRvBinding.txtItemRvPassportSeriya.text = citizens.passportSeriya
            itemRvBinding.txtItemRvPosition.text = "${position+1}"
            itemRvBinding.cardItemRv.setOnClickListener {
                rvOnClick.itemOnClick(citizens, position)
            }
            itemRvBinding.itemRvMore.setOnClickListener {
                rvOnClick.moreOnClick(citizens, position, itemRvBinding.itemRvMore)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(list[position], position)
    }

    override fun getItemCount(): Int = list.size

}

interface RvOnClick{
    fun itemOnClick(citizens: Citizens, position:Int)
    fun moreOnClick(citizens: Citizens, position: Int, v: ImageView)
}