package com.tomczyn.linkding.features.home

import com.rickclephas.kmm.viewmodel.KMMViewModel
import com.rickclephas.kmm.viewmodel.MutableStateFlow
import com.rickclephas.kmm.viewmodel.coroutineScope
import com.rickclephas.kmp.nativecoroutines.NativeCoroutinesState
import com.tomczyn.linkding.common.Launched
import com.tomczyn.linkding.common.stateInMerge
import com.tomczyn.linkding.data.LinkdingRepository
import com.tomczyn.linkding.features.home.usecase.GetHomeScreenTagsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class HomeViewModel : KMMViewModel(), KoinComponent {

    private val repo: LinkdingRepository by inject()
    private val tagsWithBookmarksUseCase: GetHomeScreenTagsUseCase by inject()

    private val _state: MutableStateFlow<HomeState> = MutableStateFlow(viewModelScope, HomeState())
        .stateInMerge(
            viewModelScope.coroutineScope,
            Launched.WhileSubscribed(5_000),
            {
                tagsWithBookmarksUseCase().onEachToState { tags, homeState ->
                    homeState.copy(tags = tags)
                }
            }
        )

    @NativeCoroutinesState
    val state: StateFlow<HomeState> get() = _state

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.coroutineScope.launch {
            repo.refreshTags()
            repo.refreshBookmarks()
        }
    }

    fun resetError() {
        _state.update { it.copy(error = false) }
    }
}
