/*
Ted Moore
ted@tedmooremusic.com
www.tedmooremusic.com
November 30, 2019

SuperCollider convenience class for creating a json file. It takes in a dictionary (or nested dictionaries) and converts them to a json file.

*/


JSONFileWriter {

	*new {
		arg object, path;
		^super.new.init(object,path);
	}

	init {
		arg object, path;
		var file = File(path,"w");
		var jsonString = this.unpackObject(object);

		file.write(jsonString);

		file.close;

		^nil;
	}

	unpackObject {
		arg object, depth = 0;
		var returnString = "";
		/*	depth.do({
		arg i;
		returnString = returnString ++ "\t";
		});*/
		returnString = returnString ++ "{\n";
		(depth+1).do({
			arg i;
			returnString = returnString ++ "\t";
		});
		object.keys.do({
			arg key, i;
			var item = object[key];
			//"object class: %".format(object.class).postln;
			case
			{item.isString || item.isKindOf(Symbol)}{
				returnString = returnString ++ "\"%\":\"%\"".format(key.asString,item);
			}
			{item.isNumber}{
				returnString = returnString ++ "\"%\":%".format(key.asString,item.asCompileString);
			}
			{item.isArray}{
				returnString = returnString ++ "\"%\":%".format(key.asString,item.asCompileString);
			}
			{item.isKindOf(Event) || item.isKindOf(Dictionary)}{
				returnString = returnString ++ "\"%\":%".format(key.asString,this.unpackObject(item,depth+1));
			}
			{
/*				"ERROR: DON'T KNOW WHAT TO DO WITH THIS:".warn;
				item.postln;
				item.class.postln;
				"".postln;*/
				returnString = item.asCompileString;
			};

			if(i != (object.keys.size-1),{
				returnString = returnString ++ ",\n";
				(depth+1).do({
					arg j;
					returnString = returnString ++ "\t";
				});
			})
		});
		returnString = "%\n".format(returnString);
		depth.do({
			arg i;
			returnString = returnString ++ "\t";
		});
		returnString = returnString ++ "}";
		^returnString;
	}
}