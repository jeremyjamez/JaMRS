/*
 * *
 *  * Created by Jeremy James on 6/16/20 10:35 AM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 6/13/20 10:31 PM
 *
 */

package xyz.james.jamrs

import android.app.Application
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.jakewharton.threetenabp.AndroidThreeTen
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import xyz.james.db.OfflineDatabase
import xyz.james.db.repositories.AppointmentRepository
import xyz.james.db.repositories.PatientRepository
import xyz.james.db.repositories.impl.AppointmentRepositoryImpl
import xyz.james.db.repositories.impl.PatientRepositoryImpl
import xyz.james.home.viewmodels.AppointmentViewModel
import xyz.james.home.viewmodels.DashboardViewModel
import xyz.james.home.viewmodels.PatientViewModel

class App : Application() {

    val appModule =  module {
        single {
            Room.databaseBuilder(applicationContext, OfflineDatabase::class.java, "jamrs")
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                .build()
        }

        single { get<OfflineDatabase>().patientDao() }
        single { get<OfflineDatabase>().appointmentDao() }

        single<PatientRepository> { PatientRepositoryImpl(get()) }
        single<AppointmentRepository> { AppointmentRepositoryImpl(get()) }

        viewModel { DashboardViewModel() }
        viewModel { PatientViewModel(patientRepository = get()) }
        viewModel { AppointmentViewModel(patientRepository = get(), appointmentRepository = get()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(appModule)
        }
    }

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `medicalRecord` " +
                    "(`medicalId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`pid` INTEGER NOT NULL, `dateRecorded` INTEGER, `bpm` INTEGER, " +
                    "`weight` REAL, `height` REAL, `pressure` TEXT, `doctorNotes` TEXT, " +
                    "FOREIGN KEY(`pid`) REFERENCES `patient`(`patientId`) ON UPDATE NO ACTION ON DELETE RESTRICT )")
        }
    }

    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `patient` " +
                    "(`patientId` TEXT NOT NULL, `firstName` TEXT NOT NULL, " +
                    "`middleName` TEXT NOT NULL, `lastName` TEXT NOT NULL, " +
                    "`dob` TEXT NOT NULL, `gender` TEXT NOT NULL, " +
                    "`medicalConditions` TEXT NOT NULL, `familyMedicalHistory` TEXT NOT NULL, " +
                    "`line1` TEXT NOT NULL, `line2` TEXT NOT NULL, `parish` TEXT NOT NULL, " +
                    "PRIMARY KEY(`patientId`))")
        }
    }

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `medicalRecord` " +
                    "(`medicalId` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                    "`pid` TEXT NOT NULL, `dateRecorded` INTEGER, `bpm` INTEGER, " +
                    "`weight` REAL, `height` REAL, `pressure` TEXT, " +
                    "`doctorNotes` TEXT, FOREIGN KEY(`pid`) REFERENCES `patient`(`patientId`) ON UPDATE NO ACTION ON DELETE RESTRICT )")

            database.execSQL("CREATE TABLE IF NOT EXISTS `appointment` " +
                    "(`appointmentId` TEXT NOT NULL, `patientId` TEXT NOT NULL, " +
                    "`appointmentDate` TEXT NOT NULL, `appointmentTime` TEXT NOT NULL, " +
                    "`appointmentNote` TEXT NOT NULL, PRIMARY KEY(`appointmentId`), " +
                    "FOREIGN KEY(`patientId`) REFERENCES `patient`(`patientId`) ON UPDATE NO ACTION ON DELETE RESTRICT )")
        }
    }
}