package com.resson.api.service;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;

public class StorageService {

  private static Storage storage = null;

  public void init() {
    storage = StorageOptions.getDefaultInstance().getService();
  }

  public Storage getBlob() {
    return storage;
  }
}