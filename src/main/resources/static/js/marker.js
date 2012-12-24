var ICONS = {
    BEAR: '/static/icons/bear.png',
    MOB: '/static/icons/mob.png',
    FARM: "/static/icons/farm.png",
    VILLAGE: "/static/icons/village.png"
}

function AgonMarker(marker) {
    this.marker = marker;
    this.init();
    this.setCurrentMarker();
}

AgonMarker.prototype.setCurrentMarker = function () {
    document.currentMarker = this;
}

AgonMarker.prototype.init = function () {
    var that = this;
    this.setType("MOB");
    google.maps.event.addListener(this.marker, "click", function () {
        that.setCurrentMarker();
    });

    google.maps.event.addListener(this.marker, 'dragend', function () {
        that.displayDetails();
    });
}
AgonMarker.prototype.update = function () {

}

AgonMarker.prototype.getId = function () {
    return this.id;
}

AgonMarker.prototype.setType = function (type) {
    this.type = type;
    var i = ICONS[type];
    this.marker.setIcon(i);
}

AgonMarker.prototype.setTitle = function (title) {
    this.marker.setTitle(title);
}
AgonMarker.prototype.displayDetails = function () {
    $('#id').val(this.getId());
    $('#title').val(this.marker.getTitle());
    $('#lat').val(this.marker.getPosition().lat());
    $('#lng').val(this.marker.getPosition().lng());
    $('#' + this.type).prop('checked', true);
}
