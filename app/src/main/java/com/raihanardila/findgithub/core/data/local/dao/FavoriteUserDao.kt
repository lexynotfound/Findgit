package com.raihanardila.findgithub.core.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raihanardila.findgithub.core.data.model.FavoriteUsersModel

@Dao
interface FavoriteUserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(fav: FavoriteUsersModel)
    @Delete
    fun delete(fav: FavoriteUsersModel)
    @Query("SELECT * from FavoriteUsersModel ORDER BY username ASC")
    fun getAllFavorite(): LiveData<List<FavoriteUsersModel>>
    @Query("SELECT * FROM FavoriteUsersModel WHERE username = :username")
    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUsersModel>

}