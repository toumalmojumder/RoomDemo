package com.toumal.roomdemo

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.toumal.roomdemo.db.Subscriber
import com.toumal.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.regex.Pattern

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

    private val statusMessage= MutableLiveData<Event<String>>()

    val message: LiveData<Event<String>>get() = statusMessage

    init{
        saveOrUpdateButtonText.value ="Save"
        clearOrDeleteButtonText.value = "Clear All"
    }
    fun saveOrUpdate(){
        if(inputName.value ==null){
            statusMessage.value= Event("Please Enter Subscriber's Name")
        }
        else if(inputEmail.value==null){
            statusMessage.value= Event("Please Enter Subscriber's Email")
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()){
            statusMessage.value= Event("Please Enter Correct Email Address")
        }
        else{
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
           val newRowId:Long = repository.insert(subscriber)
        if (newRowId>-1){
            statusMessage.value= Event("Subscriber Inserted Successfully")
        }
        else{
            statusMessage.value= Event("Error Occurred")
        }

        }
    fun update(subscriber: Subscriber):Job =  viewModelScope.launch {

        val noOfRow:Int =  repository.update(subscriber)
        if(noOfRow>0){
            inputName.value = null
            inputEmail.value = null
            isUpdateAndDelete = false

            saveOrUpdateButtonText.value = "Save"
            clearOrDeleteButtonText.value = "Clear All"
            statusMessage.value=Event("$noOfRow Row Updated Successfully")
        }
        else{
            statusMessage.value=Event("Error Occurred")
        }



    }
    fun delete(subscriber: Subscriber):Job =  viewModelScope.launch {
        val noOfRowDeleted:Int = repository.delete(subscriber)

        if (noOfRowDeleted>0){
            inputName.value = null
            inputEmail.value = null
            isUpdateAndDelete = false

            saveOrUpdateButtonText.value = "Save"
            clearOrDeleteButtonText.value = "Clear All"

            statusMessage.value=Event("$noOfRowDeleted Row Deleted Successfully")
        }
        else{
            statusMessage.value=Event("Error Occurred")
        }

    }
    fun clearAll():Job=viewModelScope.launch {
        val noOfRowDeleted:Int = repository.deleteAll()
        if(noOfRowDeleted>0){
            statusMessage.value=Event("$noOfRowDeleted Deleted Successfully")
        }
       else{
            statusMessage.value=Event("Error Occurred")
        }
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
