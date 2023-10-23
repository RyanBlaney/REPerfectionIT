package com.example.application.views.reliatype.classes;

/**
 * DataStorage an object created used to store the save data of a book.
 * This is used for reading mode only.
 * It saves one book per object.
 * 
 * @author Ryan Blaney
 * @updated 12/16/2023
 */

import java.io.Serializable;

public class DataStorage implements Serializable {
    String bookName;
    int sectionNumber, numberOfSections;

}
