package com.example.roomwordsample.repository

import androidx.annotation.WorkerThread
import com.example.roomwordsample.dataBase.Word
import com.example.roomwordsample.dataBase.WordDao
import kotlinx.coroutines.flow.Flow

class WordRepository(private val wordDao: WordDao) {

    //DAO는 전체 데이터베이스가 아닌 저장소 생성자에 전달된다. DAO 에 데이터베이스의 모든 읽기/쓰기 메소드가 포함되어 있으므로,
    //DAO 엑세스만 필요하기 때문. 전체 데이터베이스를 노출할 필요가 없다.

    val allWords: Flow<List<Word>> = wordDao.getAlphabetizedWords()
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Word){
        wordDao.insert(word)
    }
}