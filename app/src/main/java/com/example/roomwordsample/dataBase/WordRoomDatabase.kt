package com.example.roomwordsample.dataBase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

//데이터베이스 이전은 이번 예제와는 관련 없으므로 exportSchema = false로 처리.
@Database(entities = [Word::class], version = 1, exportSchema = false)
abstract class WordRoomDatabase: RoomDatabase() {
    abstract  fun wordDao(): WordDao
    private class WordDatabaseCallback(private val scope: CoroutineScope): RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let{ database ->
                scope.launch { populateDatabase(database.wordDao()) }
            }
        }
        suspend fun populateDatabase(wordDao: WordDao){
            wordDao.deleteAll()

            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World")
            wordDao.insert(word)
            word = Word("Kwandoll")
            wordDao.insert(word)
        }
    }
    companion object{
        //데이터 베이스의 여러 인스턴스가 동시에 열리는 것을 막기 위해 싱글톤으로 정의
        @Volatile
        private var INSTANCE: WordRoomDatabase? = null

        //싱글톤 반환 함수
        fun getDatabase(context: Context, scope: CoroutineScope): WordRoomDatabase{
            //만약 INSTANCE 가 null 이 아니면 즉, 이미 생성된 데이터베이스가 있으면 이를 반환하고
            //그게 아니라면 즉, 처음 만드는 거라면 데이터베이스를 생성한다.
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    WordRoomDatabase::class.java,
                    "word_database"
                ).addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }


}