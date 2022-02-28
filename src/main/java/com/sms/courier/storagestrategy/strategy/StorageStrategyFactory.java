package com.sms.courier.storagestrategy.strategy;

public interface StorageStrategyFactory {

    FileStorageService fetchStorageStrategy();
}
