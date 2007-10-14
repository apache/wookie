function search(e,q)
{
	if(e.keyCode != 13)
		return;
	var engine = widget.preferenceForKey('default_engine');
	var url = getURL(engine,q);
	
	widget.openURL(url);
}

function loadOptions()
{
	var container = document.getElementById('options');
	
	var table = document.createElement('table');
	table.width = '240px';
	
	var counter = 0;
	for(var i=0; i<engines.length; i++)
	{
		if(counter == 0)
			var tr = document.createElement('tr');
			
		var td = document.createElement('td');
		td.align = 'center';
		td.style.border = '1px solid #000000';
		td.style.paddingTop = '2px'
		td.width = '20px';
		td.height = '17px';
		td.name = engines[i];
		td.style.cursor = 'pointer';
		td.innerHTML = "<img src='Images/logos_container/"+engines[i]+".png' />";

		td.onmouseover = function () {
			this.style.borderColor = '#ccc';
			if(this.name == 'mine') {
				if(!myPre)
					document.getElementById('title_td').innerHTML = 'Add your own';
				else
					document.getElementById('title_td').innerHTML = myName;
			}
			else
				document.getElementById('title_td').innerHTML = this.name;
		};
		td.onmouseout = function() {
			this.style.borderColor = '#000';
			if(widget.preferenceForKey('default_engine') == 'mine')
				document.getElementById('title_td').innerHTML = widget.preferenceForKey('myName');
			else
				document.getElementById('title_td').innerHTML = widget.preferenceForKey('default_engine');
		};
		td.onclick = function() {
			if(this.name == 'mine') {
				if(!myPre)
				{
					document.getElementById('opt_container').parentNode.removeChild(document.getElementById('opt_container'));
					showBack(1);
					return;
				}
				widget.setPreferenceForKey(this.name,"default_engine");
				document.getElementById('search_image').src = 'Images/logos/'+this.name+'.png';
				widget.setPreferenceForKey(this.name,"default_engine");
				document.getElementById('front').removeChild(document.getElementById('opt_container'));

			}
			else
			{
				document.getElementById('search_image').src = 'Images/logos/'+this.name+'.png';
				widget.setPreferenceForKey(this.name,"default_engine");
				document.getElementById('opt_container').parentNode.removeChild(document.getElementById('opt_container'));
			}
			document.getElementById('searchfield').focus();
		};

		tr.appendChild(td);

		counter++;
		if(counter == 10 || i == engines.length-1)
		{
			if(counter != 10)
				tr.appendChild(document.createElement('td'))
			table.appendChild(tr);
			counter = 0;
		}
	}

	container.appendChild(table);
}

function drawOptionContainer()
{
	/* Check to see if the container already exists */
	if(document.getElementById('opt_container'))
	{
		document.getElementById('front').removeChild(document.getElementById('opt_container'));
		return;
	}
	
	var table = document.createElement('table');
	table.id = 'opt_container';
	table.width = '253px';
	table.cellSpacing = '0';
	table.cellPadding = '0';
	table.border = '0';
	table.style.position = 'fixed';
	table.style.left = '8px';
	table.style.top = '37px';
	
	
	var top_tr = document.createElement('tr');
	var top_td = document.createElement('td');
	top_td.style.height = '20px';
	top_td.style.fontSize = '4px';
	top_td.style.backgroundImage = 'url(Images/popup_table/top.png)'
	top_td.innerHTML = "&nbsp;";
	top_tr.appendChild(top_td);
	table.appendChild(top_tr);
	
	var defTitle = widget.preferenceForKey('default_engine');
	if(defTitle == 'mine')
		defTitle = widget.preferenceForKey('myName');
	var title_tr = document.createElement('tr');
	var title_td = document.createElement('td');
	title_td.id = 'title_td';
	title_td.style.backgroundImage = 'url(Images/popup_table/title.png)'
	title_td.innerHTML = defTitle;
	title_td.style.height = '19px';
	title_td.style.verticalAlign = 'middle';
	title_td.style.borderBottom = '1px solid #c8c8c8';
	title_td.style.fontFamily = 'sans-serif';
	title_td.style.fontWeight = 'bold';
	title_td.style.paddingLeft = '6px';
	title_td.style.color = '#ffffff';
	title_td.style.fontSize = '11px';
	title_tr.appendChild(title_td);
	table.appendChild(title_tr);

	var cont_tr = document.createElement('tr');
	var cont_td = document.createElement('td');
	cont_td.id = 'options';
	cont_td.style.backgroundImage = 'url(Images/popup_table/middle.png)';
	cont_td.style.fontFamily = 'sans-serif';
	cont_td.style.padding = '4px';
	cont_td.style.color = '#ffffff';
	cont_td.style.fontSize = '11px';
	cont_tr.appendChild(cont_td);
	table.appendChild(cont_tr);

	var bottom_tr = document.createElement('tr');
	var bottom_td = document.createElement('td');
	bottom_td.style.backgroundImage = 'url(Images/popup_table/bottom.png)'
	bottom_td.innerHTML = "&nbsp;";
	bottom_td.style.height = '11px';
	bottom_td.style.fontSize = '8px';
	bottom_tr.appendChild(bottom_td);
	table.appendChild(bottom_tr);

	document.getElementById('front').appendChild(table);
	document.getElementById('front').focus();
	loadOptions();
}
