package com.googleApp.shoppinglistapplication.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.googleApp.shoppinglistapplication.db.AppDatabase;
import com.googleApp.shoppinglistapplication.db.Items;

import java.util.List;

public class ShowItemsListViewModel extends AndroidViewModel {

    private MutableLiveData<List<Items>> listOfItems;
    private AppDatabase appDatabase;
    public ShowItemsListViewModel(Application application){
        super(application);
        listOfItems = new MutableLiveData<>();
        appDatabase = AppDatabase.getDBinstance(getApplication().getApplicationContext());

    }

    public MutableLiveData<List<Items>> getItemListObserbver(){
        return listOfItems;
    }

    public void getAllItemsList(int categoryId){
        List<Items> itemsList = appDatabase.shoppingListDao().getAllItemList(categoryId);
        if(itemsList.size()>0){
            listOfItems.postValue(itemsList);
        }
        else {
            listOfItems.postValue(null);
        }
    }
    public void insertItems(Items items){

        appDatabase.shoppingListDao().insertItems(items);
        getAllItemsList(items.categoryId);
    }
    public void updateItems(Items items){

        appDatabase.shoppingListDao().updateItems(items);
        getAllItemsList(items.categoryId);
    }
    public void deleteItems(Items items){

        appDatabase.shoppingListDao().deleteItems(items);
        getAllItemsList(items.categoryId);
    }

}
