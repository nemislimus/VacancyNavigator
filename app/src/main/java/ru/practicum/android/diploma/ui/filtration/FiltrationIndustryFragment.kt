package ru.practicum.android.diploma.ui.filtration

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import ru.practicum.android.diploma.databinding.FragmentFiltrationIndustryBinding
import ru.practicum.android.diploma.domain.models.Industry
import ru.practicum.android.diploma.ui.filtration.adapters.IndustryAdapter
import ru.practicum.android.diploma.ui.utils.BindingFragment
import ru.practicum.android.diploma.util.EMPTY_STRING

class FiltrationIndustryFragment : BindingFragment<FragmentFiltrationIndustryBinding>() {

    private val listAdapter = IndustryAdapter { clickOnIndustry(it) }

    private val mockList = listOf(
        Industry("5.461", "Авиаперевозки", null),
        Industry("5.462", "Автомобильные перевозки", null),
        Industry("5.463", "Железнодорожные перевозки", null),
        Industry("5.465", "Транспортно-логистические комплексы, порты (воздушный, водный, железнодорожный)", null),
        Industry("5.467", "Курьерская, почтовая доставка", null),
        Industry("7.538", "Интернет-провайдер", null),
        Industry(
            "7.539",
            "Системная интеграция,  автоматизации технологических и бизнес-процессов предприятия," +
                " ИТ-консалтинг",
            null
        ),
        Industry("7.540", "Разработка программного обеспечения", null),
        Industry(
            "7.541",
            "Интернет-компания (поисковики, платежные системы, соц.сети, " +
                "информационно-познавательные и развлекательные ресурсы, продвижение сайтов и прочее)",
            null
        ),
        Industry("9.399", "Мобильная связь", null),
        Industry("11", "СМИ, маркетинг, реклама, BTL, PR, дизайн, продюсирование", null),
        Industry("11.453", "Производство мультимедиа, контента, редакторская деятельность", null),
    )

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFiltrationIndustryBinding {
        return FragmentFiltrationIndustryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listAdapter.submitList(mockList)

        with(binding) {
            tbIndustryToolBar.setOnClickListener { findNavController().navigateUp() }
            rvIndustryist.adapter = listAdapter
            llSearchIndustryField.ivClearIcon.setOnClickListener {
                clearQuery()
            }

            llSearchIndustryField.etSearchQueryText.addTextChangedListener { s ->
                setSearchIcon(s.isNullOrBlank())
            }
        }

    }

    private fun setSearchIcon(queryIsEmpty: Boolean) {
        with(binding.llSearchIndustryField) {
            ivSearchIcon.isVisible = queryIsEmpty
            ivClearIcon.isVisible = !queryIsEmpty
        }
    }

    private fun clearQuery() {
        with(binding) {
            llSearchIndustryField.etSearchQueryText.setText(EMPTY_STRING)
        }
        closeKeyboard()
    }

    private fun closeKeyboard() {
        activity?.let {
            it.currentFocus?.let { view ->
                val manager = requireActivity().baseContext.getSystemService(
                    INPUT_METHOD_SERVICE
                ) as InputMethodManager
                manager.hideSoftInputFromWindow(
                    view.windowToken,
                    0
                )
            }
        }
    }

    private fun clickOnIndustry(industry: Industry) {
        binding.btnSelectIndustry.isVisible = true
        // сохраняем значение в фильтр
    }
}
