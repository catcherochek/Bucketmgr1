package com.catchersoft.bucketmgr.tools.DB.Manager;

/**
 * Created by Клим on 09.04.2018.
 */

interface IDatabase {
    void Add(String name);
    void Delete();
    void Update(String name, int id);
    String Getname();
    int Getid();

}
