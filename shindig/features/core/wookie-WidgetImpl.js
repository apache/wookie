
// Provide a default path to dwr.engine
if (dwr == null) var dwr = {};
if (dwr.engine == null) dwr.engine = {};
if (DWREngine == null) var DWREngine = dwr.engine;

if (WidgetImpl == null) var WidgetImpl = {};
WidgetImpl._path = '/wookie/dwr';
WidgetImpl.preferences = function(p0, callback) {
  dwr.engine._execute(WidgetImpl._path, 'WidgetImpl', 'preferences', p0, callback);
}
WidgetImpl.preferenceForKey = function(p0, p1, callback) {
  dwr.engine._execute(WidgetImpl._path, 'WidgetImpl', 'preferenceForKey', p0, p1, callback);
}
WidgetImpl.sharedDataForKey = function(p0, p1, callback) {
  dwr.engine._execute(WidgetImpl._path, 'WidgetImpl', 'sharedDataForKey', p0, p1, callback);
}
WidgetImpl.setPreferenceForKey = function(p0, p1, p2, callback) {
  dwr.engine._execute(WidgetImpl._path, 'WidgetImpl', 'setPreferenceForKey', p0, p1, p2, callback);
}
WidgetImpl.setSharedDataForKey = function(p0, p1, p2, callback) {
  dwr.engine._execute(WidgetImpl._path, 'WidgetImpl', 'setSharedDataForKey', p0, p1, p2, callback);
}
WidgetImpl.appendSharedDataForKey = function(p0, p1, p2, callback) {
  dwr.engine._execute(WidgetImpl._path, 'WidgetImpl', 'appendSharedDataForKey', p0, p1, p2, callback);
}
WidgetImpl.openURL = function(p0, callback) {
  dwr.engine._execute(WidgetImpl._path, 'WidgetImpl', 'openURL', p0, callback);
}
WidgetImpl.lock = function(p0, callback) {
  dwr.engine._execute(WidgetImpl._path, 'WidgetImpl', 'lock', p0, callback);
}
WidgetImpl.show = function(p0, callback) {
  dwr.engine._execute(WidgetImpl._path, 'WidgetImpl', 'show', p0, callback);
}
WidgetImpl.hide = function(p0, callback) {
  dwr.engine._execute(WidgetImpl._path, 'WidgetImpl', 'hide', p0, callback);
}
WidgetImpl.unlock = function(p0, callback) {
  dwr.engine._execute(WidgetImpl._path, 'WidgetImpl', 'unlock', p0, callback);
}