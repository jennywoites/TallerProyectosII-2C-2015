//ParseCloud Delete Old Entitis
Parse.Cloud.job("deleteMascotaEnAdopcion", function(request, status) {

	var MascotaEnAdopcion = Parse.Object.extend("MascotaEnAdopcion");
	var query = new Parse.Query(MascotaEnAdopcion);

	var day = new Date();
	day.setDate(day.getDate() - 365);
	
	var counterMascotaEnAdopcion = 1;
	query.limit = 1000;
	query.lessThan("createdAt", day);

	query.each(function(object) {
		// Delete the object
		object.destroy({
			// If the object deletion was a success
			success : function(object) {
				// Logging
				console.log(counterMascotaEnAdopcion + " Macota en Adopcion Eliminada");
				// Increment the counter
				counterMascotaEnAdopcion += 1;
			},

			// If the object deletion failed
			error : function(object, error) {

			}
		});

	}).then(function() {

		// Return a success response
		status.success("Operation Exitosa Eliminando Mascota en Adopcion");

	}, function(error) {

		// Log the error
		console.log("Error Eliminando Mascota en Adopcion: " + error);
	});
});

Parse.Cloud.job("deleteSolicitudMascotaEnAdopcion", function(request, status) {

	var SolicitudMascotaEnAdopcion = Parse.Object.extend("SolicitudMascotaEnAdopcion");
	var query = new Parse.Query(SolicitudMascotaEnAdopcion);

	var day = new Date();
	day.setDate(day.getDate() - 365);
	
	var counterMascotaEnAdopcion = 1;
	query.limit = 1000;
	query.lessThan("createdAt", day);

	query.each(function(object) {
		// Delete the object
		object.destroy({
			// If the object deletion was a success
			success : function(object) {
				// Logging
				console.log(counterMascotaEnAdopcion + " Solicitud Macota en Adopcion Eliminada");
				// Increment the counter
				counterMascotaEnAdopcion += 1;
			},

			// If the object deletion failed
			error : function(object, error) {

			}
		});

	}).then(function() {

		// Return a success response
		status.success("Operation Exitosa Eliminando Solicitud Mascota en Adopcion");

	}, function(error) {

		// Log the error
		console.log("Error Eliminando Mascota en Adopcion: " + error);
	});
});
Parse.Cloud.job("deleteMascotaPerdida", function(request, status) {

	var MascotaPerdida = Parse.Object.extend("MascotaPerdida");
	var query = new Parse.Query(MascotaPerdida);

	var day = new Date();
	day.setDate(day.getDate() - 365);
	
	var counterMascotaEnAdopcion = 1;
	query.limit = 1000;
	query.lessThan("createdAt", day);

	query.each(function(object) {
		// Delete the object
		object.destroy({
			// If the object deletion was a success
			success : function(object) {
				// Logging
				console.log(counterMascotaEnAdopcion + " Macota Perdida Eliminada");
				// Increment the counter
				counterMascotaEnAdopcion += 1;
			},

			// If the object deletion failed
			error : function(object, error) {

			}
		});

	}).then(function() {

		// Return a success response
		status.success("Operation Exitosa Eliminando Mascota Perdida");

	}, function(error) {

		// Log the error
		console.log("Error Eliminando Mascota Perdida: " + error);
	});
});

Parse.Cloud.job("deleteSolicitudMascotaPerdida", function(request, status) {

	var SolicitudMascotaPerdida = Parse.Object.extend("SolicitudMascotaPerdida");
	var query = new Parse.Query(SolicitudMascotaPerdida);

	var day = new Date();
	day.setDate(day.getDate() - 365);
	
	var counterMascotaEnAdopcion = 1;
	query.limit = 1000;
	query.lessThan("createdAt", day);

	query.each(function(object) {
		// Delete the object
		object.destroy({
			// If the object deletion was a success
			success : function(object) {
				// Logging
				console.log(counterMascotaEnAdopcion + " Solicitud Macota Perdida Eliminada");
				// Increment the counter
				counterMascotaEnAdopcion += 1;
			},

			// If the object deletion failed
			error : function(object, error) {

			}
		});

	}).then(function() {

		// Return a success response
		status.success("Operation Exitosa Eliminando Solicitud Mascota Perdida");

	}, function(error) {

		// Log the error
		console.log("Error Eliminando Mascota Perdida: " + error);
	});
});
Parse.Cloud.job("deleteMascotaEncontrada", function(request, status) {

	var MascotaEncontrada = Parse.Object.extend("MascotaEncontrada");
	var query = new Parse.Query(MascotaEncontrada);

	var day = new Date();
	day.setDate(day.getDate() - 365);
	
	var counterMascotaEnAdopcion = 1;
	query.limit = 1000;
	query.lessThan("createdAt", day);

	query.each(function(object) {
		// Delete the object
		object.destroy({
			// If the object deletion was a success
			success : function(object) {
				// Logging
				console.log(counterMascotaEnAdopcion + " Macota Encontrada Eliminada");
				// Increment the counter
				counterMascotaEnAdopcion += 1;
			},

			// If the object deletion failed
			error : function(object, error) {

			}
		});

	}).then(function() {

		// Return a success response
		status.success("Operation Exitosa Eliminando Mascota Encontrada");

	}, function(error) {

		// Log the error
		console.log("Error Eliminando Mascota Encontrada: " + error);
	});
});

Parse.Cloud.job("deleteSolicitudMascotaEncontrada", function(request, status) {

	var SolicitudMascotaEncontrada = Parse.Object.extend("SolicitudMascotaEncontrada");
	var query = new Parse.Query(SolicitudMascotaEncontrada);

	var day = new Date();
	day.setDate(day.getDate() - 365);
	
	var counterMascotaEnAdopcion = 1;
	query.limit = 1000;
	query.lessThan("createdAt", day);

	query.each(function(object) {
		// Delete the object
		object.destroy({
			// If the object deletion was a success
			success : function(object) {
				// Logging
				console.log(counterMascotaEnAdopcion + " Solicitud Macota Encontrada Eliminada");
				// Increment the counter
				counterMascotaEnAdopcion += 1;
			},

			// If the object deletion failed
			error : function(object, error) {

			}
		});

	}).then(function() {

		// Return a success response
		status.success("Operation Exitosa Eliminando Solicitud Mascota Encontrada");

	}, function(error) {

		// Log the error
		console.log("Error Eliminando Mascota Encontrada: " + error);
	});
});
Parse.Cloud.job("deleteComments", function(request, status) {

	var Comentarios = Parse.Object.extend("Comentarios");
	var query = new Parse.Query(Comentarios);

	var day = new Date();
	day.setDate(day.getDate() - 365);
	
	var counterMascotaEnAdopcion = 1;
	query.limit = 1000;
	query.lessThan("createdAt", day);

	query.each(function(object) {
		// Delete the object
		object.destroy({
			// If the object deletion was a success
			success : function(object) {
				// Logging
				console.log(counterMascotaEnAdopcion + " Comentarios Eliminados");
				// Increment the counter
				counterMascotaEnAdopcion += 1;
			},

			// If the object deletion failed
			error : function(object, error) {

			}
		});

	}).then(function() {

		// Return a success response
		status.success("Operation Exitosa Eliminando Comentarios");

	}, function(error) {

		// Log the error
		console.log("Error Eliminando Comentarios: " + error);
	});
});
Parse.Cloud.job("unPublishAdopcion", function(request, status) {

	var MascotaEnAdopcion = Parse.Object.extend("MascotaEnAdopcion");
	var query = new Parse.Query(MascotaEnAdopcion);

	var day = new Date();
	day.setDate(day.getDate() - 30);
	
	var counterMascotaEnAdopcion = 1;
	query.limit = 1000;
	query.lessThan("createdAt", day);
	query.equalTo("state","PUBLICADA");

	query.each(function(object) {
		// Update the object
		
		object.set("state","OCULTA");
		object.save();
		
	}).then(function() {

		// Return a success response
		status.success("Operation Despublicando Adopcion");

	}, function(error) {

		// Log the error
		console.log("Error Despublicando Adopcion: " + error);
	});
});
