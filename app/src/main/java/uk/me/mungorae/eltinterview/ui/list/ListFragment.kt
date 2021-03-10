package uk.me.mungorae.eltinterview.ui.list

import android.content.Intent
import android.content.res.ColorStateList
import android.net.*
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import uk.me.mungorae.eltinterview.App
import uk.me.mungorae.eltinterview.R
import uk.me.mungorae.eltinterview.api.Type
import uk.me.mungorae.eltinterview.databinding.ListFragmentBinding
import javax.inject.Inject

class ListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by viewModels<ListViewModel> { viewModelFactory }

    private var _binding: ListFragmentBinding? = null
    private val binding
        get() = _binding
            ?: throw IllegalStateException("This property is only valid between onCreateView and onDestroyView.")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (requireActivity().application as App).appComponent().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = ListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.listToolbar.inflateMenu(R.menu.list)
        binding.listToolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_list_refresh -> viewModel.onRefreshButtonClicked()
                else -> return@setOnMenuItemClickListener false
            }
            true
        }
        binding.listListView.adapter = ListAdapter(layoutInflater)
        binding.listOfflineButton.setOnClickListener {
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            (binding.listListView.adapter as ListAdapter).setTasks(it)
        }
        viewModel.noItemsVisible.observe(viewLifecycleOwner) {
            binding.listTextNoItems.setVisible(it)
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.listProgressLoading.setVisible(it)
        }
        viewModel.showOffline.observe(viewLifecycleOwner) {
            binding.listBannerConnection.setVisible(it)
        }
        viewModel.showError.observe(viewLifecycleOwner) {
            binding.listViewError.setVisible(it !is ListViewModel.Error.None)
            binding.listTextError.text = when (it) {
                ListViewModel.Error.ConnectionError -> getString(R.string.list_error_connection)
                ListViewModel.Error.DatabaseError -> getString(R.string.list_error_database)
                ListViewModel.Error.None -> ""
            }
        }
        viewModel.filterItems.observe(viewLifecycleOwner) { filters ->
            binding.listViewFilter.setVisible(filters.isNotEmpty())
            binding.listViewFilter.removeAllViews()
            filters.forEach { filter ->
                layoutInflater
                    .inflate(R.layout.button_filter_item, binding.listViewFilter, false)
                    .let { it as ImageButton }
                    .apply {
                        when (filter.type) {
                            Type.GENERAL -> setImageResource(R.drawable.general)
                            Type.HYDRATION -> setImageResource(R.drawable.hydration)
                            Type.MEDICATION -> setImageResource(R.drawable.medication)
                            Type.NUTRITION -> setImageResource(R.drawable.nutrition)
                        }
                        setOnClickListener {
                            viewModel.onFilterItemSelected(filter)
                        }
                        imageTintList = if (filter.selected) {
                            ColorStateList.valueOf(
                                ContextCompat.getColor(requireContext(), R.color.purple_500)
                            )
                        } else null
                    }
                    .let { binding.listViewFilter.addView(it) }
            }
        }

        viewModel.onViewCreated()
    }

    override fun onDestroyView() {
        viewModel.onViewDestroyed()
        super.onDestroyView()
    }

    private fun View.setVisible(visible: Boolean) {
        visibility = if (visible) View.VISIBLE else View.GONE
    }

    companion object {
        fun newInstance() = ListFragment()
    }
}