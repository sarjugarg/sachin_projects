package com.giftingnetwork.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ItemNotFoundException extends RuntimeException {

   Logger logger = LoggerFactory.getLogger(ItemNotFoundException.class);

   private static final long serialVersionUID = 1L;

   public ItemNotFoundException(String message) {
      super(message);
   }

   @ExceptionHandler
   @ResponseStatus(HttpStatus.NOT_FOUND)

   public void handleINFException(ItemNotFoundException ex) {

      logger.info(" Handle With Care  " + ex);

   }

}