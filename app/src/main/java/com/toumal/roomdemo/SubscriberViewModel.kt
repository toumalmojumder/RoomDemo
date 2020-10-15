package com.toumal.roomdemo

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toumal.roomdemo.db.Subscriber
import com.toumal.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository):ViewModel(),Observable {
    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    val subscribers= repository.subscribers

    @Bindable
    val inputName = MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()
    @Bindable
    val saveOrUpdateButtonText = MutableLiveData<String>()
    @Bindable
    val clearOrDeleteButtonText = MutableLiveData<String>()

    init{
        saveOrUpdateButtonText.value ="Save"
        clearOrDeleteButtonText.value = "Clear All"
    }
    fun saveOrUpdate(){
        val name:String = inputName.value!!
        val email:String = inputEmail.value!!
        insert(Subscriber(0,name,email))
        inputName.value = null
        inputEmail.value = null
    }
    fun clearOrDelete(){
        clearAll()

    }
    fun insert(subscriber: Subscriber):Job =  viewModelScope.launch {

            repository.insert(subscriber)
        }
    fun update(subscriber: Subscriber):Job =  viewModelScope.launch {

        repository.update(subscriber)
    }
    fun delete(subscriber: Subscriber):Job =  viewModelScope.launch {

        repository.delete(subscriber)
    }
    fun clearAll():Job=viewModelScope.launch {

        repository.deleteAll()
    }


    }
