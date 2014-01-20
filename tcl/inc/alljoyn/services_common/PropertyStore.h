/******************************************************************************
 * Copyright (c) 2013, AllSeen Alliance. All rights reserved.
 *
 *    Permission to use, copy, modify, and/or distribute this software for any
 *    purpose with or without fee is hereby granted, provided that the above
 *    copyright notice and this permission notice appear in all copies.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES
 *    WITH REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF
 *    MERCHANTABILITY AND FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR
 *    ANY SPECIAL, DIRECT, INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES
 *    WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, WHETHER IN AN
 *    ACTION OF CONTRACT, NEGLIGENCE OR OTHER TORTIOUS ACTION, ARISING OUT OF
 *    OR IN CONNECTION WITH THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ******************************************************************************/

#ifndef _PROPERTY_STORE_H_
#define _PROPERTY_STORE_H_

#include <PropertyStoreOEMProvisioning.h>

/** @defgroup PropertyStore
 *
 *  @{
 */

/**
 * read all properties
 * @param reply
 * @param property_store_filter_t
 * @param langIndex
 * @return status
 */
AJ_Status PropertyStore_ReadAll(AJ_Message* reply, property_store_filter_t filter, enum_lang_indecies_t langIndex);

/**
 * update given property
 * @param key
 * @param langIndex
 * @param value
 * @return status
 */
AJ_Status PropertyStore_Update(const char* key, enum_lang_indecies_t langIndex, const char* value);

/**
 * reset given property back to default
 * @param key
 * @param langIndex
 * @return status
 */
AJ_Status PropertyStore_Reset(const char* key, enum_lang_indecies_t langIndex);

/**
 * reset all properties back to defaults
 * @return status
 */
AJ_Status PropertyStore_ResetAll();

/**
 * initialize the store
 */
void PropertyStore_Init();

/**
 * get field name for given field index
 * @param fieldIndex
 * @return fieldName
 */
const char* PropertyStore_GetFieldNameForIndex(enum_field_indecies_t fieldIndex);

/**
 * get field index for given field name
 * @param fieldName
 * @return fieldIndex
 */
enum_field_indecies_t PropertyStore_GetIndexOfField(const char* fieldName);

/**
 * get value for given field index for given language index
 * @param fieldIndex
 * @param langIndex
 * @return value
 */
const char* PropertyStore_GetValueForLang(enum_field_indecies_t fieldIndex, enum_lang_indecies_t langIndex);

/**
 * get value for field index for default language
 * @param fieldIndex
 * @return value
 */
const char* PropertyStore_GetValue(enum_field_indecies_t fieldIndex);

/**
 * set value for given field index for given language index
 * @param fieldIndex
 * @param langIndex
 * @param value
 * @return success
 */
uint8_t PropertyStore_SetValueForLang(enum_field_indecies_t fieldIndex, enum_lang_indecies_t langIndex, const char* value);

/**
 * set value for given field index for the default language
 * @param fieldIndex
 * @param value
 * @return success
 */
uint8_t PropertyStore_SetValue(enum_field_indecies_t fieldIndex, const char* value);

/**
 * get default language index among all languages indexes
 * @return langIndex
 */
enum_lang_indecies_t PropertyStore_GetCurrentDefaultLanguageIndex();

/**
 * returns the language index for the given language name
 * @param language
 * @return langIndex
 */
enum_lang_indecies_t PropertyStore_GetLanguageIndex(const char* const language);

/**
 * load all persisted values
 */
AJ_Status PropertyStore_LoadAll();

/**
 * save all persisted values
 */
AJ_Status PropertyStore_SaveAll();

/**
 * clear persisted values and load all factory defaults
 */
void PropertyStore_FactoryReset();

/** @} */
 #endif /* _PROPERTY_STORE_H_ */