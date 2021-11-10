package io.tux.wallet.testnet.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import io.tux.wallet.testnet.R
import io.tux.wallet.testnet.adapter.NotificationAdapter
import io.tux.wallet.testnet.databinding.FragmentNotificationBinding
import io.tux.wallet.testnet.model.NotificationModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class NotificationFragment : Fragment(), View.OnClickListener {
lateinit var binding : FragmentNotificationBinding
var list = ArrayList<NotificationModel>()
    lateinit var adapter : NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBinding.inflate(layoutInflater)
        binding.ivBack.setOnClickListener(this)
        setNotifications()
        return binding.root
    }


    private fun setNotifications()
    {
        val today = Date()
        val format = SimpleDateFormat("yyyy-MM-dd hh:mm:ss a")
        val dateToStr: String = format.format(today)


        list.add(NotificationModel("Notification1",resources.getString(R.string.dummy_text),"comment",dateToStr))
        list.add(NotificationModel("Notification2",resources.getString(R.string.dummy_text),"request",dateToStr))
        list.add(NotificationModel("Notification3",resources.getString(R.string.dummy_text),"message",dateToStr))
        list.add(NotificationModel("Notification4",resources.getString(R.string.dummy_text),"message",dateToStr))
        list.add(NotificationModel("Notification5",resources.getString(R.string.dummy_text),"request",dateToStr))
        list.add(NotificationModel("Notification6",resources.getString(R.string.dummy_text),"comment",dateToStr))
//        adapter = NotificationAdapter(requireContext(),list)
        binding.recyclerview.apply {
            var linearLayoutManager =LinearLayoutManager(context)
            linearLayoutManager.orientation = VERTICAL
            layoutManager = LinearLayoutManager(context)
            adapter = NotificationAdapter(context,list)
        }
    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.iv_back -> findNavController().popBackStack()
        }
    }
}