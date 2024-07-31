package com.github.theapache64.now.di.module;

import com.github.theapache64.now.data.repo.DateTimeRepo;
import com.github.theapache64.now.data.repo.DateTimeRepoImpl;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent::class)
abstract class RepoModule {
    @Binds
    abstract fun bindDateTimeRepo(dateTimeRepoImpl: DateTimeRepoImpl): DateTimeRepo
}