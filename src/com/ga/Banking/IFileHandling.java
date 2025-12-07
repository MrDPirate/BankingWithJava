package com.ga.Banking;

import java.util.List;

public interface IFileHandling {
    String path = "db/";
    public void addNew();
    public List<String> getFileNamesIn_db();
}
