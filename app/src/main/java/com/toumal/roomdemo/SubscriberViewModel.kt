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
    private var isUpdateAndDelete= false
    private lateinit var subscriberToUpdateOrDelete: Subscriber


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
        if(isUpdateAndDelete){
            subscriberToUpdateOrDelete.name = inputName.value!!
            subscriberToUpdateOrDelete.email = inputEmail.value!!
            update(subscriberToUpdateOrDelete)

        }
        else{
            val name:String = inputName.value!!
            val email:String = inputEmail.value!!
            insert(Subscriber(0,name,email))
            inputName.value = null
            inputEmail.value = null
        }



    }
    fun clearOrDelete(){
        if(isUpdateAndDelete){
            delete(subscriberToUpdateOrDelete)
        }
        else{
            clearAll()
        }

    }


    fun insert(subscriber: Subscriber):Job =  viewModelScope.launch {

            repository.insert(subscriber)
        }
    fun update(subscriber: Subscriber):Job =  viewModelScope.launch {

        repository.update(subscriber)

        inputName.value = null
        inputEmail.value = null
        isUpdateAndDelete = false

        saveOrUpdateButtonText.value = "Save"
        clearOrDeleteButtonText.value = "Clear All"
    }
    fun delete(subscriber: Subscriber):Job =  viewModelScope.launch {
        repository.delete(subscriber)
        inputName.value = null
        inputEmail.value = null
        isUpdateAndDelete = false

        saveOrUpdateButtonText.value = "Save"
        clearOrDeleteButtonText.value = "Clear All"
    }
    fun clearAll():Job=viewModelScope.launch {

        repository.deleteAll()
    }

    fun initUpdateAndDelete(subscriber: Subscriber){
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateAndDelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        clearOrDeleteButtonText.value = "Delete"
    }

    }
