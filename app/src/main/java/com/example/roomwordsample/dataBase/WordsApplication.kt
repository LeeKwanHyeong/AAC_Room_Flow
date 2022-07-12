package com.example.roomwordsample.dataBase

import android.app.Application
import com.example.roomwordsample.repository.WordRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.supervisorScope

class WordsApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    //데이터베이스 인스턴스 생성
    val database by lazy { WordRoomDatabase.getDatabase(this, applicationScope)}
    //데이터베이스 DAO에 기반하여 저장소 인스턴스를 생성
    val repository by lazy { WordRepository(database.wordDao()) }
}