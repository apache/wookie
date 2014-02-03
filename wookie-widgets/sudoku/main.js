/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
window.DEBUG = false;

// constant values
window.PUZZLE_KEY = 'PUZZLE';
window.SOLUTION_KEY = 'SOLUTION';
window.CELL_STATE_RE = /^cell_([0-9]+)$/;
window.PENALTY_STATE_RE = /^penalty_(.+)$/;

// sudoku generator object
window.generator = null;

// game data
window.puzzleArray = [];
window.solutionArray = [];
window.liveGameArray = [];
window.playerRecords = [];

// status messages on top
window.messages = [];
window.MAX_MESSAGES = 5;

//
// Set up the game
//
function init() {
  window.generator = new sudoku.SudokuGenerator();

  //
  // If in DEBUG mode, show the
  // debug console
  //
  if (window.DEBUG) {
    $('#debugConsole').css({display: 'block'});
    initClickHandlers();    
  } else {
    $('#debugConsole').css({display: 'none'});
  }

  if (window.wave) {
    //
    // Start collaborative play
    //
    wave.setParticipantCallback(function() {
        updateMessages();
        updateRankingDisplay();
    });
    wave.setStateCallback(waveStateChanged); 
  }
}

//
// Called whenever we get a state changed event.
//
// There are two kinds of events we need to respond to:
// 1. Another player has moved
// 2. Another player (or this player) has started a new game
//
function waveStateChanged() {
  //
  // Check if the game has reset by comparing the current solution
  // in the window with the wave state solution
  //  
  if (get("SOLUTION") && get("SOLUTION") != JSON.stringify(window.solutionArray) ) {
    debug("game was reset");
    
    //
    // Set new game
    //
    window.puzzleArray = JSON.parse(get(PUZZLE_KEY));
    window.solutionArray = JSON.parse(get(SOLUTION_KEY));
    window.liveGameArray = window.puzzleArray.concat([]); // clone liveGameArray from puzzleArray  
    showGame();
    
    //
    // Clear game messages
    //
    window.messages = [];
    updateMessages();
      
  } else {
    //
    // Check for another player's move
    //
    updateGameProgress();   
  }
}

//
// Update game board with events
// from wave state
//
function updateGameProgress() {
  var keys = wave.getState().getKeys();
  for (var i = 0; i < keys.length; ++i) {   
    var key = keys[i];
    
    if (key.match(window.CELL_STATE_RE)) {
      var arrayIndex = RegExp.$1;
      // only close this cell if it is still opened
      if (window.liveGameArray[arrayIndex] == 0) {
        var playerName = get(key);
        closeCell(arrayIndex, getCellValue(arrayIndex), playerName);
      }
    } else {
      if (key.match(window.PENALTY_STATE_RE)) {        
        var playerName = RegExp.$1;
        //playerName = playerName.replace(/\*/g, ' ');
        // handle the penalty count
        var penalty = get(key);        
        debug('penalty for ' + playerName + '=' + penalty);

        var playerRecord = getPlayerRecord(playerName);

        if (playerRecord == null) {
          playerRecord = newPlayerRecord(playerName);
        }

        if (playerRecord.penalty != penalty) {                
          playerRecord.penalty = penalty;
          var score = playerRecord.points - playerRecord.penalty;    
          updateRankingDisplay();  
          appendMessage('<div class="playername">'+playerName + '</div><span class="message incorrect"> -1</span>');
        }        
      }
    }
  }
}

function showGame() {
  $('#splash').hide();
  displaySudoku(window.puzzleArray.join(''));
  updateGameProgress();
}

function newGame(){
    var data = generateNewPuzzle();
    
    if (window.wave){
        //
        // Create the game data and send it, but don't change our local state
        //
        debug("created new game - submitting delta");
        var delta = {};
        delta[PUZZLE_KEY] = JSON.stringify(data.puzzle);
        delta[SOLUTION_KEY] = JSON.stringify(data.solution);
        //
        // Clear keys for each cell
        //
        for (var i=0;i<81;i++){
            delta["cell_"+i] = null;
        }
        wave.getState().submitDelta(delta);
    } else {
        //
        // In single player mode, just start the game
        //
        window.puzzleArray = data.puzzle;
        window.solutionArray = data.solution;
        window.liveGameArray = window.puzzleArray.concat([]); // clone liveGameArray from puzzleArray  
        showGame();
    }
}

//
// Create the puzzle
//
function generateNewPuzzle() {
  var data = {};
  try {
    var generator = new sudoku.SudokuGenerator();
    var puzzleString = generator.generate();

    data.puzzle = generator.partialArray;
    data.solution = generator.fullArray;

    if (puzzleString == null) {  
      data = generateNewPuzzle();
    }     
  } catch (e) {
    data = generateNewPuzzle();
  }
  return data;
}

function onGameOver() { 
  if (confirm('Game over. New game?')) {
    //
    // Reset all the game data
    //
    if (window.wave) {
        // reset all game data
        window.puzzleArray = [];
        window.solutionArray = [];
        window.liveGameArray = [];
        window.messages = [];
   
        // clear info
        updateMessages();
        updateRankingDisplay();
    }  
    newGame();
  }
}

function closeCell(arrayIndex, value, playerName) {
  var cell = $('#cell_' + arrayIndex);
  cell.html(value);
  cell.addClass('solutionCell');  

  // make sure to unbind click handler
  cell.unbind('click');

  // update liveGameArray
  window.liveGameArray[arrayIndex] = value;

  addPoint(playerName);

  var playerRecord = getPlayerRecord(playerName);

  var score = playerRecord.points - playerRecord.penalty;
  appendMessage('<div class="playername">'+playerName + '</div><div class="message correct"> +1</div>');

  if (isGameOver()) {                
    // update game state to be over
    debug('game over');
    onGameOver();
  }
}


//
// Check if game over
//
function isGameOver() { 
  if (liveGameArray.length == 0) {
    // the game isn't fully initialized yet
    return false;
  } else {
    return window.liveGameArray.join('') == window.solutionArray.join('');
  }
}


function onRightMove(arrayIndex, value) {  
  // update the local live game array
  window.liveGameArray[arrayIndex] = value;
  // report state change to save
  // TODO
  set('cell_' + arrayIndex, getViewer());
  closeCell(arrayIndex, value, getViewer());
}

function onWrongMove() {  
  var key = 'penalty_' + getViewer(); //.replace(/ /g, '*');
  var value = get(key);
  if (value == null) {
    value = 0
  }
  value++;
  set(key, value);  
}

function blurOnCell(inputBox) {
  var userInput = parseInt(inputBox.val());//inputBox.attr('value');
  var cell = inputBox.parent();
 
  var cellId = cell.attr('id');
  var arrayIndex = parseInt(cellId.replace('cell_', ''));     

  if (isInputCorrect(arrayIndex, userInput)) { 
    onRightMove(arrayIndex, userInput);
  } else {     
    onWrongMove();
    cell.empty();
    cell.bind('click', handleCellInput);  
  }
}

function getCellValue(arrayIndex) {
  return window.solutionArray[arrayIndex];
}

function isInputCorrect(arrayIndex, input) {
  return (getCellValue(arrayIndex) == input);
}

function handleCellInput() {
        
  var arrayIndex = parseInt($(this).attr('id').replace('cell_', ''));  
  
  debug('ans: ' + getCellValue(arrayIndex));

  $(this).unbind('click');
         
  //clearAnnounce();      
        
  var cell = $(this);
        
  var cellValue = (cell.html() == '')?'':'value=' +cell.html();    

  var inputBox = $('<input maxlength=1 ' + cellValue 
    + ' type=text id=cellOpenBox>');
        
  cell.html(inputBox);
        
  inputBox.focus();
  inputBox.select();
          
  inputBox.blur(function () {
    var number = parseInt(inputBox.val());
    debug('user ans=' + number);
     if (isNaN(number)) {
       cell.empty();
       cell.bind('click', handleCellInput); 
     } else {
       if (number >= 1 && number <= 9) {
         inputBox.unbind();
         blurOnCell(inputBox);
       }
     }
  });
  
  inputBox.keyup(function() {
    var number = parseInt(inputBox.val());
    debug('user ans=' + number);
     if (isNaN(number)) {
       cell.empty();
       cell.bind('click', handleCellInput); 
     } else {
       if (number >= 1 && number <= 9) {
         inputBox.unbind();
         blurOnCell(inputBox);
       }
     }
  });          
}


//
// Show the game board
//
function displaySudoku(sudokuStr) {
  
  //
  // Hide the game display
  //
  $('#display').hide();
  
  //
  // Remove event listeners on cells
  // and existing CSS that shows/hides
  // the scores
  //
  for (var i=0;i<81;i++) {
    var cellId = '#cell_' + i;
    $(cellId).unbind();
    $(cellId).removeClass('blankCell');
    $(cellId).removeClass('givenCell');
    $(cellId).removeClass('solutionCell');  
  }
  
  //
  // Set the cell values
  //
  for (var i=0;i<sudokuStr.length;i++) {
    var value = sudokuStr.charAt(i);
    var cellId = '#cell_' + i;
    if (value == 0) {
      // this is a blank cell      
      $(cellId).click(handleCellInput);            
      $(cellId).html('');  
    } else {  
      // this is a given cell
      $(cellId).html(value);  
    }
  }
  
  //
  // Show the game display
  // and messages display
  //
  $('#display').fadeIn(500);
  $('#info').fadeIn(500);
}

//////////////////////////////
// Wave convenience methods // 
//////////////////////////////

function get(key) {
  var ret = null;
  if (window.wave) {
    ret = wave.getState().get(key); 
  }
  return ret;
}

function set(key, value) {
  if (window.wave) {
    wave.getState().submitValue(key,value);
  }  
}

function getViewer() {
  var ret = "averylongname";
  if (window.wave) {
    ret = wave.getViewer().getDisplayName();
  }
  return ret;
}

///////////////
// PLAYERS //
///////////////

function appendMessage(msg) {
  if (messages.length >= MAX_MESSAGES) {
    messages.pop();    
  }
  messages.unshift(msg);
  updateMessages();
}

function updateMessages() {
  var html = [];
  html.push('<b>Updates:</b><br><br>');
  html.push(messages.join(''));

  $('#messages').html(html.join(''));
}

function newPlayerRecord(playerName) {
  playerRecord = {};
  playerRecord.name = playerName;
  playerRecord.points = 0;
  playerRecord.penalty = 0;
  playerRecords.push(playerRecord);
  return playerRecord;
}

function addPoint(playerName) {
  var playerRecord = getPlayerRecord(playerName);
  if (playerRecord == null) {
    playerRecord = newPlayerRecord(playerName);
  }  
  playerRecord.points++;
  updateRankingDisplay();
}

function updateRankingDisplay() {
  
  // sort player record by points
  playerRecords.sort(function(a, b) {
    return (b.points - b.penalty) - (a.points - a.penalty);
  });  

  var html = [];
  html.push('<b>Ranking:</b><br><br>');
  
  for (var i = 0; i < playerRecords.length; i++) {
    var playerRecord = playerRecords[i];
    var name = playerRecord.name;
    var total = playerRecord.points - playerRecord.penalty;
    var rank = i + 1;
    html.push('<div class="playername">'+ name + '</div><div class="message">' + total + '</div>');
  }

  $('#rankingDisplay').html(html.join(''));
}

function getPlayerRecord(playerName) {
  var ret = null;

  for (var i = 0; i < playerRecords.length; i++) {
    var record = playerRecords[i];
    if (record.name == playerName) {
      ret = record;
      break;
    }
  }

  return ret;
}

///////////////
// DEBUGGING //
///////////////

function debug(msg) {
  $('#debug').prepend(msg + '<br/>');
}

function initClickHandlers() {
  
  $('#clear').click(function() {
    $('#debug').empty();
  });

  $('#print').click(function() {
    printAllStates();
  });
  
  $('#cheat').click(function() {
    cheat();
  });
}

function cheat(){
  onGameOver();
}

function printAllStates() {  

  if (!window.wave) {
    return;
  }

  var html = [];

  var keys = wave.getState().getKeys();

  for (var i = 0; i < keys.length; ++i) {   
    var key = keys[i];
    var value = get(key);

    html.push(key + ' = ' + value);
    html.push('<br>');
  }
  
  debug(html.join(''));
}
