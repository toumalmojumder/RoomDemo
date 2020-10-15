package com.toumal.roomdemo.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SubscriberDAO {
    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber) :Long
/*
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubscriber2(subscriber: Subscriber) :Long

    @Insert
    fun insertSubscriber(subscriber1: Subscriber,subscriber2: Subscriber,subscriber3: Subscriber,) :List<Long>

    @Insert
    fun insertSubscriber(subscriber:List <Subscriber>):List<Long>

    @Insert
    fun insertSubscriber2(subscriber: Subscriber, subscribers:List <Subscriber>):List<Long>
*/

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber):Int

    @Delete
    suspend fun deleteSubscriber(subscriber: Subscriber) :Int

    @Query("DELETE FROM SUBSCRIBER_DATA_TABLE")
    suspend fun deleteAll() :Int

    @Query("SELECT * FROM SUBSCRIBER_DATA_TABLE")
    fun getAllSubscriber():LiveData<List<Subscriber>>
}