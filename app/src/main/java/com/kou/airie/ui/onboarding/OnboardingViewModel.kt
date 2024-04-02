package com.kou.airie.ui.onboarding

import androidx.lifecycle.ViewModel
import com.kou.airie.data.AirieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val airieRepository: AirieRepository
) : ViewModel(){
}