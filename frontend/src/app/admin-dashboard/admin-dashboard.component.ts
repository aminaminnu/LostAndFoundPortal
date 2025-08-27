import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AdminService, AppUser } from '../services/admin.service';
import { ClaimService, ClaimDto } from '../services/claim.service';   // you expose these

@Component({
  selector   : 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls  : ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent implements OnInit, OnDestroy {

  /* ─── sidebar tabs ────────────────────────────────────────── */
  tabs = [
    { id:'users',   icon:'people',          label:'All users' },
    { id:'finders', icon:'travel_explore',  label:'Finders'   },
    { id:'losers',  icon:'sentiment_dissatisfied', label:'Losers' }
    
  ];
  active  = 'users';
  heading = 'All users';

  /* ─── data holders ────────────────────────────────────────── */
  list:     AppUser[]  = [];   // full user list
  filtered: AppUser[]  = [];   // after search / type filter
  claims:   ClaimDto[] = [];

  displayed = ['id','name','email','role','userType','enabled','act'];

  /* ─── search / filter ─────────────────────────────────────── */
  search     = '';
  roleFilter = '';

  constructor(private apiUsers  : AdminService,
              private apiClaims : ClaimService,
              private snack     : MatSnackBar) {}

  /* ───────── lifecycle ───────── */
  ngOnInit(): void {
    document.body.classList.add('admin-shell');
    this.loadUsers();
  }
  ngOnDestroy(): void {
    document.body.classList.remove('admin-shell');
  }

  /* ───────── NAV HANDLER ───────── */
  switch(tab:string){
    this.active  = tab;
    this.heading = tab === 'users'
      ? 'All users'
      : tab === 'finders'
      ? 'Finders'
      : tab === 'losers'
      ? 'Losers'
      : '—';

    if(tab === 'claims') { this.loadClaims(); }
    else                 { this.loadUsers(tab); }
  }
  loadClaims() {
    throw new Error('Method not implemented.');
  }

  /* ───────── LOADERS ──────────── */
  private loadUsers(filterType?: string) {
  this.apiUsers.listUsers().subscribe(data => {

    /* 🔸 translate the tab id → enum literal */
    let match: string | undefined;
    if (filterType === 'finders')      match = 'FINDER';
    else if (filterType === 'losers')  match = 'LOSER';
    else if (filterType === 'users')   match = undefined;   // show all

    /* 🔸 apply filter (or none) */
    this.list = !match
      ? data
      : data.filter(u => u.userType === match);

    this.applyFilter();                // keep search box working
  });
}




  /* ───────── SEARCH/FILTER ────── */
  applyFilter(){
    const term = this.search.trim().toLowerCase();
    this.filtered = this.list.filter(u =>
       (!this.roleFilter || u.role === this.roleFilter) &&
       (!term ||
         u.name?.toLowerCase().includes(term) ||
         u.email?.toLowerCase().includes(term))
    );
  }
  viewIsUsers(){ return this.active !== 'claims'; }

  /* ───────── USER ACTIONS ─────── */
  save(u:AppUser){
    this.apiUsers.updateUser(u).subscribe(
      _ => this.snack.open('Saved','Close',{duration:2000}),
      _ => this.snack.open('Save failed','Close',{duration:2000})
    );
  }
  remove(u:AppUser){
    if(!confirm(`Delete ${u.email}?`)) return;
    this.apiUsers.deleteUser(u.id).subscribe(
      _ => { this.snack.open('Deleted','Close',{duration:2000}); this.loadUsers(this.active); },
      _ => this.snack.open('Delete failed','Close',{duration:2000})
    );
  }

}
