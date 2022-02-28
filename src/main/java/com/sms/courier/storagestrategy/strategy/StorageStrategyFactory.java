package com.sms.courier.storagestrategy.strategy;

import com.sms.courier.storagestrategy.StorageType;

public interface StorageStrategyFactory {

    FileStorageService fetchStorageStrategy();

    FileStorageService fetchStorageStrategy(StorageType type);
}
