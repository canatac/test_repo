function AT(){}
function vT(){}
function p4(){}
function f6(){}
function Qzb(){}
function Pzb(){}
function Lac(){}
function _ac(){}
function dbc(){}
function hbc(){}
function lbc(){}
function abc(b){this.b=b}
function ebc(b){this.b=b}
function ibc(b){bb();this.b=b}
function Zac(b,c){b.enctype=c;b.encoding=c}
function gEb(b,c){b.onload=zsc(function(){c.ag()})}
function g6(){var b;this.Mb=(b=$doc.createElement(tuc),b.type=Duc,b)}
function mbc(b){var c;this.b=b;this.Mb=(c=$doc.createElement(tuc),c.type='file',c);this.Mb[euc]='gwt-FileUpload'}
function Nac(b){SFb(b.n,false);b.o||(b.e.Mb[ouc]=true,undefined);b.d=false}
function Oac(b){SFb(b.n,true);b.e.Mb[ouc]=false;b.d=true;if(b.o){Mac(b);b.o=false}}
function Mac(b){if(b.p){$doc.body.removeChild(b.p);b.p.onload=null;b.p=null}}
function Rac(b,c){if(b.f!=c){b.f=c;if(b.f){z0(b.e,1024);z0(b.e,2048)}}j0(b.Mb,'v-upload-immediate',b.f)}
function Qac(b){_0(b.k,b.n);_0(b.k,b.e);b.e=new mbc(b);b.e.Mb.name=b.j+FEc;b.e.Mb[ouc]=!b.d;a5(b.k,b.e);a5(b.k,b.n);b.f&&z0(b.e,1024)}
function CT(){yT=new AT;Ub((Sb(),Rb),34);!!$stats&&$stats(xc(EEc,Isc,-1,-1));yT.Oc();!!$stats&&$stats(xc(EEc,LAc,-1,-1))}
function zT(){var b,c,d;while(wT){d=nb;wT=wT.b;!wT&&(xT=null);if(!d){(Fwb(),Ewb).Ag(CG,new Qzb);nob()}else{try{(Fwb(),Ewb).Ag(CG,new Qzb);nob()}catch(b){b=KJ(b);if(Ar(b,37)){c=b;_tb.Oe(c)}else throw b}}}}
function Sac(b){if(b.e.Mb.value.length==0||b.o||!b.d){_tb.Qe('Submit cancelled (disabled, no file or already submitted)');return}Hlb(b.b);b.c.submit();b.o=true;_tb.Qe('Submitted form');Nac(b);b.q=new ibc(b);db(b.q,800)}
function Pac(b){var c;if(!b.p){c=$doc.createElement(Buc);c.innerHTML="<iframe src=\"javascript:''\" name='"+b.j+"_TGT_FRAME' style='position:absolute;width:0;height:0;border:0'>"||Dsc;b.p=je(c);$doc.body.appendChild(b.p);b.c.target=b.j+'_TGT_FRAME';gEb(b.p,b)}}
function Tac(){this.Mb=$doc.createElement('form');this.e=new mbc(this);this.k=new d5;this.g=new g6;this.c=this.Mb;Zac(this.Mb,SCc);this.c.method='post';n3(this,this.k);a5(this.k,this.g);a5(this.k,this.e);this.n=new VFb;q0(this.n,new abc(this),(Dk(),Dk(),Ck));a5(this.k,this.n);this.Mb[euc]='v-upload';this.Jb==-1?YZ(this.Mb,241|(this.Mb.__eventBits||0)):(this.Jb|=241)}
var FEc='_file',GEc='buttoncaption',EEc='runCallbacks34';_=AT.prototype=vT.prototype=new J;_.gC=function BT(){return Nv};_.Oc=function FT(){zT()};_.cM={};_=p4.prototype=new O_;_.gC=function q4(){return sx};_.Tc=function r4(b){u0(this,b)};_.cM={10:1,13:1,15:1,22:1,69:1,70:1};_=g6.prototype=f6.prototype=new O_;_.gC=function h6(){return Hx};_.cM={10:1,13:1,15:1,22:1,69:1,70:1};_=Qzb.prototype=Pzb.prototype=new J;_.Ye=function Rzb(){return new Tac};_.gC=function Szb(){return LB};_.cM={139:1};_=Tac.prototype=Lac.prototype=new k3;_.gC=function Uac(){return CG};_.ld=function Vac(){t0(this);!!this.b&&Pac(this)};_.Tc=function Wac(b){(Y$(b.type)&241)>0&&(Ivb(this.b.I,b,this,null),undefined);u0(this,b)};_.md=function Xac(){v0(this);this.o||Mac(this)};_.ag=function Yac(){zvb((Gc(),Fc),new ebc(this))};_.ge=function $ac(b,c){var d;if(Plb(c,this,b,true)){return}if('notStarted' in b[1]){db(this.q,400);return}if('forceSubmit' in b[1]){Sac(this);return}Rac(this,Boolean(b[1][Gvc]));this.b=c;this.j=b[1][xuc];this.i=b[1]['nextid'];d=Mlb(c,b[1][Jvc][lzc]);this.c.action=d;if(GEc in b[1]){UFb(this.n,b[1][GEc]);this.n.Mb.style.display=Dsc}else{this.n.Mb.style.display=ctc}this.e.Mb.name=this.j+FEc;if(ouc in b[1]||Dvc in b[1]){Nac(this)}else if(!Boolean(b[1][Yvc])){Oac(this);Pac(this)}};_.cM={10:1,13:1,15:1,17:1,19:1,20:1,21:1,22:1,26:1,33:1,69:1,70:1,75:1,76:1};_.b=null;_.c=null;_.d=true;_.f=false;_.i=0;_.j=null;_.n=null;_.o=false;_.p=null;_.q=null;_=abc.prototype=_ac.prototype=new J;_.gC=function bbc(){return yG};_.jc=function cbc(b){this.b.f?(this.b.e.Mb.click(),undefined):Sac(this.b)};_.cM={12:1,39:1};_.b=null;_=ebc.prototype=dbc.prototype=new J;_.Xb=function fbc(){if(this.b.o){if(this.b.b){!!this.b.q&&cb(this.b.q);_tb.Qe('VUpload:Submit complete');Hlb(this.b.b)}Qac(this.b);this.b.o=false;Oac(this.b);this.b.Ib||Mac(this.b)}};_.gC=function gbc(){return zG};_.cM={3:1,14:1};_.b=null;_=ibc.prototype=hbc.prototype=new $;_.gC=function jbc(){return AG};_.Tb=function kbc(){_tb.Qe('Visiting server to see if upload started event changed UI.');dlb(this.b.b,this.b.j,'pollForStart',Dsc+this.b.i,true,105)};_.cM={65:1};_.b=null;_=mbc.prototype=lbc.prototype=new p4;_.gC=function nbc(){return BG};_.Tc=function obc(b){u0(this,b);if(Y$(b.type)==1024){this.b.f&&this.b.e.Mb.value!=null&&!ikc(Dsc,this.b.e.Mb.value)&&Sac(this.b)}else if((ypb(),!xpb&&(xpb=new Xpb),ypb(),xpb).b.i&&Y$(b.type)==2048){this.b.e.Mb.click();this.b.e.Mb.blur()}};_.cM={10:1,13:1,15:1,22:1,69:1,70:1};_.b=null;var Nv=Pic(vAc,'AsyncLoader34'),sx=Pic(yAc,'FileUpload'),Hx=Pic(yAc,'Hidden'),LB=Pic(FAc,'WidgetMapImpl$48$1'),yG=Pic(DAc,'VUpload$1'),zG=Pic(DAc,'VUpload$2'),AG=Pic(DAc,'VUpload$3'),BG=Pic(DAc,'VUpload$MyFileUpload');zsc(CT)();