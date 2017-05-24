
**Introducción**
============

¡En simples pasos vas a poder integrar nuestra solución para recibir pagos en tu aplicación!

Utiliza nuestra SDK e instantáneamente ofrecerás a tus usuarios:
 
 - Pagos con tarjeta en una o más cuotas, efectivo o transferencia bancaria. 
 - Comunicación de descuentos y promociones.
 - Comunicación del resultado de su pago.
 - Guardado de sus tarjetas más usadas.


Añade la dependencia a tu proyecto
----------------------------------
[Android]
Añade la dependencia en el archivo **build.gradle** del módulo donde nos integres con el siguiente código: 

    dependencies {
       compile 'com.mercadopago:sdk:3.0.0'
    }

Sino puedes [descargar el SDK](https://github.com/mercadopago/px-android/releases) y añadirlo en tu proyecto.

----------
[iOS - Swift/Objective-C]

Si en tu proyecto utilizas **CocoaPods**, puedes añadir la dependencia en el **Podfile** del módulo donde nos integres con el siguiente código:

    source 'https://github.com/CocoaPods/Specs.git'
	#Se necesita este parámetro por ser una SDK en swift
    use_frameworks!
    platform :ios, '8.2'
    pod 'MercadoPagoSDK', '3.0.0'

Sino puedes [descargar el SDK](https://github.com/mercadopago/px-ios/releases) y añadirlo a tu proyecto.

----------

**Recibiendo Pagos**
============

## Crea un botón de pago

#### A modo de ejemplo proponemos que inicies el flujo de MercadoPago desde un botón.

[Android]
> 1. Crea un Activity para insertar el botón (**MainActivity**, por ejemplo).  
> 2. Agrega un campo de texto para mostrar el resultado del pago. 
> 3. Pega el siguiente código de ejemplo en **res/layout/activity_main.xml**.

    <FrameLayout xmlns:android='http://schemas.android.com/apk/res/android'
             xmlns:tools='http://schemas.android.com/tools'
             android:layout_width='match_parent'
             android:layout_height='match_parent'
             android:paddingLeft='@dimen/activity_horizontal_margin'
             android:paddingRight='@dimen/activity_horizontal_margin'
             android:paddingTop='@dimen/activity_vertical_margin'
             android:paddingBottom='@dimen/activity_vertical_margin'
             android:orientation='vertical'
             tools:context='.MainActivity'>
	    <include layout="@layout/mpsdk_view_progress_bar"/>
	    <LinearLayout
	            android:id="@+id/mpsdkRegularLayout"
	            android:layout_width="match_parent"
	            android:layout_height="match_parent"
	            android:orientation="vertical">

	        <Button
	                android:layout_width='match_parent'
	                android:layout_height='50dp'
	                android:layout_marginTop='25dp'
	                android:gravity='center'
	                android:text='Pagar $10'
	                android:onClick='submit'/>

	        <TextView
	                android:layout_width='match_parent'
	                android:layout_height='wrap_content'
	                android:id='@+id/mp_results'
	                android:paddingTop='50dp'/>
	    </LinearLayout>
    </FrameLayout>

----------
[iOS - Swift]
> 1. Crea un ViewController para insertar el botón (**MainViewController**, por ejemplo).
>2.  Inserta un botón en el **.xib** correspondiente.
> 3. Agrega un campo de texto para mostrar el resultado del pago.
> 4. Pega el siguiente código de ejemplo en tu clase **MainViewController.swift**.
> 5. En el siguiente paso estarás trabajando sobre el evento asociado al click botón (startCheckout).

	import UIKit
	import MercadoPagoSDK
	
	   class MainViewController: UIViewController {
     @IBOutlet weak var payButton: UIButton!
     @IBOutlet weak var paymentResult: UITextField!

     override func viewDidLoad() {
       super.viewDidLoad()

       self.payButton.addTarget(self,
         action: #selector(MainViewController.startCheckout),
         for: .touchUpInside)
	     }
     }
   
----------
[iOS - Objective]
> 1. Crea un ViewController para insertar el botón (**MainViewController**, por ejemplo).
>2.  Inserta un botón en el .xib correspondiente.
> 3. Agrega un campo de texto (en nuestro caso lo llamamos paymentResult) para mostrar el resultado del pago.
> 4. Pega el siguiente código de ejemplo en tu clase **MainViewController.swift**.
> 5. En el siguiente paso estarás trabajando sobre el evento asociado al click botón (startCheckout).


	@import MercadoPagoSDK;

	 @interface MainExamplesViewController()
	 @property (weak, nonatomic) IBOutlet UIButton *button;
	 @property (weak, nonatomic) IBOutlet UILabel *label;

	 @end
	 @implementation MainExamplesViewController

	   - (void)viewDidLoad {
       [super viewDidLoad];

       [_button addTarget:self action:@selector(startCheckout:)
           forControlEvents:UIControlEventTouchUpInside];
	   }
	 @end

----------

### Crea una Preferencia de Pago en los servidores de MercadoPago

Una [Preferencia de Pago](https://www.mercadopago.com.ar/developers/en/api-docs/basic-checkout/checkout-preferences/) contiene todo el detalle de información del producto o servicio que se va a pagar. Por ejemplo:

> 1. Datos y monto de lo que se va a pagar.
> 2. Datos de tu comprador.
> 3. Medios de pago que aceptas.
> 4. ID de referencia en tu sistema.

### ¿Como crear una Preferencia de Pago?

#### Primero:

> 1. [Registra la cuenta de MercadoPago](https://registration.mercadopago.com.ar/registration-mp?mode=mp) dónde recibirás el dinero.
> 2. [Crea una aplicación.](https://applications.mercadopago.com.ar/list?platform=mp)
> 3. [Configura tus credenciales.](https://www.mercadopago.com/mla/account/credentials?type=basic)

#### Luego, en tu Servidor:

[Crea una preferencia de pago](https://www.mercadopago.com.ar/developers/es/solutions/payments/basic-checkout/receive-payments/) y retorna la respuesta que te dan nuestros servicios. Hazlo desde tu servidor, porque tendrás que firmarla con tu clave privada. De esta forma nos aseguramos proteger tanto al comprador, como a tu propio usuario vendedor. 

En tu servidor, al crear la preferencia, podrás configurar si deseas excluir algún medio de pago y si deseas una cantidad de cuotas por defecto.

Las preferencias completas funcionan mucho mejor. Envíanos toda la información que puedas y verás que ofrecerás tan buena experiencia para los compradores, que **¡tendrás más pagos acreditados!**

> **Tip:** Puedes probar [nuestras SDKs](https://www.mercadopago.com.ar/developers/es/tools/) del lado Servidor.

#### Por último, en tu Aplicación:
En el SDK te ofrecemos una clase llamada **CustomServer** que se conecta con tu servidor. El método createPreference hace un POST y envía como cuerpo del mensaje el mapa que hayas definido (preferenceMap). Indícanos tu URL base (https://api.tunombre.com) y la URI (/create_preference) donde esperas los datos para crear la preferencia.

CustomServer se encargará de transformar la respuesta de tu servicio (la misma que los servicios de Mercado Pago) en un objeto **CheckoutPreference**, que cuyo ID es el punto de entrada a nuestro checkout.

Crea la preferencia en tu servidor desde tu aplicación con el siguiente código:

[Android]

    public void submit(View view) {
        // Crea un mapa con los datos de la compra y el mail de tu cliente.
        Map<String, Object> preferenceMap = new HashMap<>();
        preferenceMap.put("item_id", "1");
        preferenceMap.put("amount", new BigDecimal(10));
        preferenceMap.put("currency_id", "ARS");
        preferenceMap.put("payer_email", "customermail@test.com");

        final Activity activity = this;
        LayoutUtil.showProgressLayout(activity);
        CustomServer.createCheckoutPreference(activity, "https://api.tunombre.com", "/create_preference", preferenceMap, new Callback<CheckoutPreference>() {
            @Override
            public void success(CheckoutPreference checkoutPreference) {
                startMercadoPagoCheckout(checkoutPreference);
                LayoutUtil.showRegularLayout(activity);
            }

            @Override
            public void failure(ApiException apiException) {
                // Ups, something went wrong
            }
        });
    }

[iOS - Swift]

	let preferenceBody : [String : Any] = ["amount" : 10,
                      "itemId" : 29334, "customerId": 207,
                      "payer_email" : "customermail@test.com"]

	let servicePreference = ServicePreference()
           servicePreference.setCreateCheckoutPreference(baseURL: “https://api.tunombre.com“, URI: “/create_preference”, additionalInfo: preferenceBody as NSDictionary)
	MercadoPagoCheckout.setServicePreference(servicePreference)

	MerchantServer.createPreference(
	  success: { (checkoutPreference) in
	      startMercadoPagoCheckout(checkoutPreference)
	}, failure: { (error) in
	     // Ups, something went wrong
	})

[iOS - Objective-C]

	NSDictionary *preferenceBody = @{
                                 @“amount” : @10,
                                 @“itemId” : @29334,
                                 @"customerId" : @207,
                                 @"payerEmail" : @"cusomermail@test.com" };

	ServicePreference * servicePreference = [[ServicePreference alloc] init];
		[servicePreference setCreateCheckoutPreferenceWithBaseURL:@"" URI:@"" additionalInfo:preferenceBody];
	[MercadoPagoCheckout setServicePreference:servicePreference];

	[MerchantServer createPreferenceWithSuccess:^(CheckoutPreference * checkoutPreference) {
        [self startMercadoPagoCheckoutWithCheckoutPreference: checkoutPreference];
    } failure:^(NSError * _Nonnull) {
        // Ups, something went wrong
    }];


###¡Inicia nuestro Checkout!
Para iniciar nuestro checkout sólo necesitas:

1. Clave pública: es un identificador único de tu cuenta, tu aplicación y sus configuraciones.
  - [Crea tus credenciales.](https://www.mercadopago.com/mla/account/credentials)
  - [Configura tu aplicación.](https://applications.mercadopago.com/)
2. Identificador de la preferencia de pago.

Una vez creada la Preferencia de Pago estás en condiciones de iniciar nuestro Checkout con el siguiente código:

[Android]

    private void startMercadoPagoCheckout(CheckoutPreference checkoutPreference) {
        new MercadoPagoCheckout.Builder()
                .setActivity(activity)
                .setPublicKey(publicKey)                .setCheckoutPreference(checkoutPreference)
                .startForPayment();
    }

[iOS - Swift]

    public func startMercadoPagoCheckout(_ checkoutPreference CheckoutPreference) {
	   let publicKey = "TEST-ad365c37-8012-4014-84f5-6c895b3f8e0a"

	   let checkout = MercadoPagoCheckout(publicKey: publicKey, accessToken: nil, checkoutPreference: checkoutPreference,
       navigationController: self.navigationController!)

       checkout.start()
 }

[iOS - Objective-C]

	    -(void)startMercadoPagoCheckout:(CheckoutPreference *)checkoutPreference {
		    self.mpCheckout = [[MercadoPagoCheckout alloc] initWithPublicKey: TEST_PUBLIC_KEY accessToken: nil checkoutPreference:checkoutPreference paymentData:nil discount:nil navigationController:self.navigationController paymentResult: nil];
    [self.mpCheckout start];
	}

### ¡Obtén la respuesta!

El SDK devolverá siempre un resultado del pago.

Si hubo algún error insalvable o el usuario abandonó el flujo, devolverá una excepción para que puedas entender qué pasó.

Estos son los atributos más importantes del pago:

- id: Identificador del pago.
- status: [Estados del pago.](https://www.mercadopago.com.ar/developers/es/api-docs/custom-checkout/webhooks/payment-status/)
- payment_method_id: Identificador del medio de pago que eligió tu usuario.
- payment_type_id: Tipo de medio elegido.
- card: Objeto que identifica la tarjeta de tu usuario.
- issuer_id: Identificador del banco de la tarjeta que eligió tu usuario.
- installments: Cantidad de cuotas elegidas.


Podrás obtener la respuesta con el siguiente código:

[Android]

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MercadoPagoCheckout.CHECKOUT_REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                Payment payment = JsonUtil.getInstance().fromJson(data.getStringExtra("payment"), Payment.class);
                ((TextView) findViewById(R.id.mp_results)).setText("Resultado del pago: " + payment.getStatus());
                //Done!
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null && data.getStringExtra("mercadoPagoError") != null) {
                    //Resolve error in checkout
                } else {
                    //Resolve canceled checkout
                }
            }
        }
    }
	    
[iOS - Swift]

	MercadoPagoCheckout.setPaymentCallback { (payment) in
    self.payment = payment
	}

	MercadoPagoCheckout.setCallback { (Void) in
    // Resolved cancel checkout
	}

[iOS - Objective-C]

	[MercadoPagoCheckout setPaymentCallbackWithPaymentCallback:^(Payment * payment) {
	self.payment = payment
	}];

	[MercadoPagoCheckout setCallbackWithCallback:^{
	// Resolved cancel checkout
	}];


**Configura tu color**
============

Puedes cambiar los colores de la interfaz gráfica de nuestro Checkout, como así también hacer más oscura la fuente utilizando la clase DecorationPreference. Esto lo puedes lograr con el siguiente código:

[Android]

	private void startMercadoPagoCheckout(CheckoutPreference checkoutPreference) {
	DecorationPreference decorationPreference = new DecorationPreference.Builder()
        .setBaseColor(ContextCompat.getColor(context, R.color.your_color))
        .enableDarkFont() //Optional
        .build();

	new MercadoPagoCheckout.Builder()
        .setActivity(activity)
       .setDecorationPreference(decorationPreference)
        .setPublicKey(publicKey)
        .setCheckoutPreference(checkoutPreference)
        .startForPayment();
	}
	
[iOS - Swift]

	public func startMercadoPagoCheckout(_ checkoutPreference CheckoutPreference) {
	   let decorationPreference: DecorationPreference = DecorationPreference()
	   decorationPreference.setBaseColor(color: UIColor.purple)
	   decorationPreference.enableDarkFont()
	   MercadoPagoCheckout.setDecorationPreference(decorationPreference)

	   let publicKey = "TEST-ad365c37-8012-4014-84f5-6c895b3f8e0a"

	   let checkout = MercadoPagoCheckout(publicKey: publicKey, accessToken: nil, checkoutPreference: checkoutPreference,
         navigationController: self.navigationController!)

	   checkout.start()
	}

[iOS - Objective-C]

	DecorationPreference *decorationPreference = [[DecorationPreference alloc] initWithBaseColor:[UIColor fromHex:@"#CA254D"]];
    [decorationPreference enableDarkFont];
    [MercadoPagoCheckout setDecorationPreference:decorationPreference];

	 -(void)startMercadoPagoCheckout:(CheckoutPreference *)checkoutPreference {
		    self.mpCheckout = [[MercadoPagoCheckout alloc] initWithPublicKey: TEST_PUBLIC_KEY accessToken: nil checkoutPreference:checkoutPreference paymentData:nil discount:nil navigationController:self.navigationController paymentResult: nil];
    [self.mpCheckout start];
	}

----------

# **Personalización**

## Paga en tu Servidor

El SDK permite que configures tu propio servicio de pagos. De esta manera no tendrás que crear una preferencia en los servidores de MercadoPago. 

En la clase ServicePreference puedes configurar la URL y la URI de tu servicio junto con un Map para que puedas enviar la información que desees.
Al momento de postear el pago, el SDK lo hará a tu servicio y esperará recibir un pago, tal como responde el servicio de MercadoPago.

Una vez creada la ServicePreference, debes iniciar el Checkout de MercadoPago, tal como se muestra en el siguiente código:

[Android]

        public void submit(View view) {
        CheckoutPreference checkoutPreference = new CheckoutPreference.Builder()
                .setSite(Sites.ARGENTINA)
                .addItem(new Item("Test Item", new BigDecimal("100")))
                .build();

        HashMap<String, Object> extraData = new HashMap<>();
        map.put("item_id", "id");

        ServicePreference servicePreference = new ServicePreference.Builder()
                .setCreatePaymentURL("https://www.tunombre.com", "/createPayment", extraData)
                .build();

        new MercadoPagoCheckout.Builder()
                .setActivity(this)
                .setServicePreference(servicePreference)
                .setPublicKey("TEST-0b74577e-863f-4a0e-9932-b87761cda03e")
                .setCheckoutPreference(checkoutPreference)
                .startForPayment();
    }

[iOS - Swift]

	let item = Item(_id: "Item_Id", title: "Remeras", quantity: 1, unitPrice: 50, description: nil, currencyId: "ARS")
	let payer = Payer(_id: "Payer_Id", email: "sarasa@gmail.com", type: nil, identification: nil, entityType: nil)

	let checkoutPreference = CheckoutPreference()
	checkoutPreference.items = [item]
	checkoutPreference.payer = payer
	checkoutPreference.setId("MLA")

	let servicePreference = ServicePreference()
	servicePreference.setCreatePayment(baseURL: "https://your-base-URL.com/", URI: "your_create_preference_URI",
    additionalInfo: ["item_id" : "id", "quantity" : 1])

	MercadoPagoCheckout.setServicePreference(servicePreference)
	
	 let checkout = MercadoPagoCheckout(publicKey: publicKey, accessToken: nil, checkoutPreference: checkoutPreference,
         navigationController: self.navigationController!)

	   checkout.start()


[iOS - Objective-C]

	 Item *item = [[Item alloc] initWith_id:@"itemId" title:@"item title 2" quantity:2 unitPrice:2 description:@"item description" currencyId:@"ARS"];
    Payer *payer = [[Payer alloc] initWith_id:@"payerId" email:@"payer@email.com" type:nil identification:nil entityType:nil];
    
    NSArray *items = [NSArray arrayWithObjects:item, item, nil];
        
    self.pref = [[CheckoutPreference alloc] initWithItems:items payer:payer paymentMethods:nil];
	[self.pref setSiteId:@“MLA”];

	ServicePreference * servicePreference = [[ServicePreference alloc] init];
	 NSDictionary *extraParams = @{
                                  @"merchant_access_token" : @"mla-cards-data" };
	[servicePreference setCreatePaymentWithBaseURL:@"https://private-0d59c-mercadopagoexamples.apiary-mock.com" URI:@"/create_payment" additionalInfo:extraParams];
	[MercadoPagoCheckout setServicePreference:servicePreference];

	-(void)startMercadoPagoCheckout:(CheckoutPreference *)checkoutPreference {
		    self.mpCheckout = [[MercadoPagoCheckout alloc] initWithPublicKey: TEST_PUBLIC_KEY accessToken: nil checkoutPreference:checkoutPreference paymentData:nil discount:nil navigationController:self.navigationController paymentResult: nil];
    [self.mpCheckout start];
	}

## Personalización de cobro

Una alternativa a crear Checkout Preferences en los servidores de Mercado Pago es recolectar la información necesaria para realizar el pago y luego finalizarlo en tus servidores.

### Crear una instancia local de una Checkout Preference

Para iniciar nuestro Checkout debes crear una instancia local de la Checkout Preference que incluya como mínimo un item y el país desde el cual se quiere realizar el pago.  A su vez, el Item debe recibir una descripción y el monto, o una descripción, cantidad y precio unitario.

[Android]
```
CheckoutPreference checkoutPreference = new CheckoutPreference.Builder()
.addItem(new Item("Item", new BigDecimal("1000")))
.setSite(Sites.ARGENTINA)
build();
```

[Swift]

	let item = Item(_id: "Item_Id", title: "Remeras", quantity: 1, unitPrice: 50, description: nil, currencyId: "ARS")
	let payer = Payer(_id: "Payer_Id", email: "sarasa@gmail.com", type: nil, identification: nil, entityType: nil)

	let checkoutPreference = CheckoutPreference()
	checkoutPreference.items = [item]
	checkoutPreference.payer = payer
	checkoutPreference.setId("MLA")

[Objective-C]
			  
			  Item *item = [[Item alloc] initWith_id:@"itemId" title:@"item title" quantity:2 unitPrice:2 description:@"item description" currencyId:@"ARS"];
    
    Payer *payer = [[Payer alloc] initWith_id:@"payerId" email:@"payer@email.com" type:nil identification:nil entityType:nil];
    
    NSArray *items = [NSArray arrayWithObjects:item, item, nil];
        
    self.pref = [[CheckoutPreference alloc] initWithItems:items payer:payer paymentMethods:nil];
	[self.pref setSiteId:@“MLA”];

### Personalizar una Checkout Preference

De ser necesario, puedes especificar restricciones dentro del objeto CheckoutPreference como exclusiones de medios o tipos de pago específicos y establecer la cantidad de cuotas máximas o por default.

#### Excluir Medios de Pago

Puedes especificar los tipos de medio de pago que no quieras soportar (Efectivo, Tarjetas de Crédito o Débito) excluyéndolos en la creación de la Checkout Preference.

*Excluir un tipo de medio de pago específico:*

[Android]
```
CheckoutPreference checkoutPreference = new CheckoutPreference.Builder()
.addItem(new Item("Item", new BigDecimal("1000")))
.setSite(Sites.ARGENTINA)
 //Excluir un tipo de medio de pago específico.
.addExcludedPaymentType(PaymentTypes.TICKET)
.build(); 
```

[iOS - Swift]
```
let item = Item(_id: "Item_Id", title: "Remeras", quantity: 1, unitPrice: 50, description: nil, currencyId: "ARS")
	let payer = Payer(_id: "Payer_Id", email: "sarasa@gmail.com", type: nil, identification: nil, entityType: nil)

	let checkoutPreference = CheckoutPreference()
	checkoutPreference.items = [item]
	checkoutPreference.payer = payer
	checkoutPreference.setId("MLA")
	checkoutPreference.setExcludedPaymentTypes(["ticket"])
```

[iOS - Objective-C]
```
	  Item *item = [[Item alloc] initWith_id:@"itemId" title:@"item title" quantity:2 unitPrice:2 description:@"item description" currencyId:@"ARS"];
    
    Payer *payer = [[Payer alloc] initWith_id:@"payerId" email:@"payer@email.com" type:nil identification:nil entityType:nil];
    
    NSArray *items = [NSArray arrayWithObjects:item, item, nil];
        	
PaymentPreference *paymentExclusions = [[PaymentPreference alloc] init];
    paymentExclusions.excludedPaymentTypeIds = [NSSet setWithObjects:@"ticket", nil];
    
    self.pref = [[CheckoutPreference alloc] initWithItems:items payer:payer paymentMethods:paymentExclusions];

	[self.pref setSiteId:@“MLA”];
```

*Excluir más de un tipo de medio de pago:*

[Android]
```
List<String> excludedPaymentTypes = new ArrayList<>();
excludedPaymentTypes.add(PaymentTypes.TICKET);
excludedPaymentTypes.add(PaymentTypes.BANK_TRANSFER);

CheckoutPreference checkoutPreference = new CheckoutPreference.Builder()
.addItem(new Item("Item", new BigDecimal("1000")))
.setSite(Sites.ARGENTINA)
//Excluir varios medios de pago.
.addExcludedPaymentTypes(excludedPaymentTypes)
.build();                                              
```

[iOS - Swift]
```
let item = Item(_id: "Item_Id", title: "Remeras", quantity: 1, unitPrice: 50, description: nil, currencyId: "ARS")
	let payer = Payer(_id: "Payer_Id", email: "sarasa@gmail.com", type: nil, identification: nil, entityType: nil)

	let checkoutPreference = CheckoutPreference()
	checkoutPreference.items = [item]
	checkoutPreference.payer = payer
	checkoutPreference.setId("MLA")
	checkoutPreference.setExcludedPaymentTypes(["ticket","bank_transfer"])
```

[iOS - Objective-C]
```
Item *item = [[Item alloc] initWith_id:@"itemId" title:@"item title" quantity:2 unitPrice:2 description:@"item description" currencyId:@"ARS"];
    
    Payer *payer = [[Payer alloc] initWith_id:@"payerId" email:@"payer@email.com" type:nil identification:nil entityType:nil];
    
    NSArray *items = [NSArray arrayWithObjects:item, item, nil];
        	
PaymentPreference *paymentExclusions = [[PaymentPreference alloc] init];
    paymentExclusions.excludedPaymentTypeIds = [NSSet setWithObjects:@"bank_transfer", @"ticket", nil];
    
    self.pref = [[CheckoutPreference alloc] initWithItems:items payer:payer paymentMethods:paymentExclusions];

	[self.pref setSiteId:@“MLA”];
```

O incluso puedes determinar qué medios de pago específicos (Visa, Mastercard, etc) quieres excluir del checkout:

*Excluir un medio de pago específico:*

[Android]
```
CheckoutPreference checkoutPreference = new CheckoutPreference.Builder()
.addItem(new Item("Item", new BigDecimal("1000")))
.setSite(Sites.ARGENTINA)
 //Excluir un medio de pago específico
.addExcludedPaymentMethod(PaymentMethods.ARGENTINA.VISA) 
.build(); 
```

[iOS - Swift]
```
let item = Item(_id: "Item_Id", title: "Remeras", quantity: 1, unitPrice: 50, description: nil, currencyId: "ARS")
	let payer = Payer(_id: "Payer_Id", email: "sarasa@gmail.com", type: nil, identification: nil, entityType: nil)

	let checkoutPreference = CheckoutPreference()
	checkoutPreference.items = [item]
	checkoutPreference.payer = payer
	checkoutPreference.setId("MLA")
	checkoutPreference.setExcludedPaymentMethods(["visa"])
```

[iOS - Objective-C]
```
Item *item = [[Item alloc] initWith_id:@"itemId" title:@"item title" quantity:2 unitPrice:2 description:@"item description" currencyId:@"ARS"];
    
    Payer *payer = [[Payer alloc] initWith_id:@"payerId" email:@"payer@email.com" type:nil identification:nil entityType:nil];
    
    NSArray *items = [NSArray arrayWithObjects:item, item, nil];
        	
PaymentPreference *paymentExclusions = [[PaymentPreference alloc] init];
    paymentExclusions.excludedPaymentMethods = [NSSet setWithObjects:@"visa", nil];
    
    self.pref = [[CheckoutPreference alloc] initWithItems:items payer:payer paymentMethods:paymentExclusions];

	[self.pref setSiteId:@“MLA”];
```

*Excluir más de un medio de pago:*

[Android]
```
List<String> excludedPaymentMethods = new ArrayList<>();
excludedPaymentMethods.add(PaymentMethods.ARGENTINA.VISA);
excludedPaymentMethods.add(PaymentMethods.ARGENTINA.MASTER);

CheckoutPreference checkoutPreference = new CheckoutPreference.Builder()
.addItem(new Item("Item", new BigDecimal("1000")))
.setSite(Sites.ARGENTINA)
//Excluir varios medios de pago
.addExcludedPaymentMethods(excludedPaymentMethods)
.build();                                              
```

[iOS - Swift]
```
let item = Item(_id: "Item_Id", title: "Remeras", quantity: 1, unitPrice: 50, description: nil, currencyId: "ARS")
	let payer = Payer(_id: "Payer_Id", email: "sarasa@gmail.com", type: nil, identification: nil, entityType: nil)

	let checkoutPreference = CheckoutPreference()
	checkoutPreference.items = [item]
	checkoutPreference.payer = payer
	checkoutPreference.setId("MLA")
	checkoutPreference.setExcludedPaymentMethods(["visa","master"])
```

[iOS - Objective-C]
```
Item *item = [[Item alloc] initWith_id:@"itemId" title:@"item title" quantity:2 unitPrice:2 description:@"item description" currencyId:@"ARS"];
    
    Payer *payer = [[Payer alloc] initWith_id:@"payerId" email:@"payer@email.com" type:nil identification:nil entityType:nil];
    
    NSArray *items = [NSArray arrayWithObjects:item, item, nil];
        	
PaymentPreference *paymentExclusions = [[PaymentPreference alloc] init];
    paymentExclusions.excludedPaymentMethods = [NSSet setWithObjects:@"visa",@"master", nil];
    
    self.pref = [[CheckoutPreference alloc] initWithItems:items payer:payer paymentMethods:paymentExclusions];

	[self.pref setSiteId:@“MLA”];
```

#### Personalizar Cuotas

Puedes precisar la cantidad máxima de cuotas que quieres soportar para tus medios de pago:

[Android]
```
CheckoutPreference checkoutPreference = new CheckoutPreference.Builder()
.addItem(new Item("Item", new BigDecimal("1000")))
.setSite(Sites.ARGENTINA)
//Limitar la cantidad de cuotas
.setMaxInstallments(1) 
.build();
```

[iOS - Swift]
```
let item = Item(_id: "Item_Id", title: "Remeras", quantity: 1, unitPrice: 50, description: nil, currencyId: "ARS")
	let payer = Payer(_id: "Payer_Id", email: "sarasa@gmail.com", type: nil, identification: nil, entityType: nil)

	let checkoutPreference = CheckoutPreference()
	checkoutPreference.items = [item]
	checkoutPreference.payer = payer
	checkoutPreference.setId("MLA")
	checkoutPreference.setMaxInstallments(1)
```

[iOS - Objective-C]
```
Item *item = [[Item alloc] initWith_id:@"itemId" title:@"item title" quantity:2 unitPrice:2 description:@"item description" currencyId:@"ARS"];
    
    Payer *payer = [[Payer alloc] initWith_id:@"payerId" email:@"payer@email.com" type:nil identification:nil entityType:nil];
    
    NSArray *items = [NSArray arrayWithObjects:item, item, nil];
        	
PaymentPreference *paymentExclusions = [[PaymentPreference alloc] init];
    paymentExclusions.setMaxInstallments = 1;
        
    self.pref = [[CheckoutPreference alloc] initWithItems:items payer:payer paymentMethods:paymentExclusions];

	[self.pref setSiteId:@“MLA”];
```

O también establecer una cantidad de coutas por default que se seleccionará automáticamente, si es que existe para el medio de pago seleccionado por el usuario. De lo contrario, se le mostrará la pantalla de coutas para que él elija:

[Android]
```
CheckoutPreference checkoutPreference = new CheckoutPreference.Builder()
.addItem(new Item("Item", new BigDecimal("1000")))
.setSite(Sites.ARGENTINA)
//Setear una cantidad de cuotas default
.setDefaultInstallments(3)
.build();
```

[iOS - Swift]
```
let item = Item(_id: "Item_Id", title: "Remeras", quantity: 1, unitPrice: 50, description: nil, currencyId: "ARS")
	let payer = Payer(_id: "Payer_Id", email: "sarasa@gmail.com", type: nil, identification: nil, entityType: nil)

	let checkoutPreference = CheckoutPreference()
	checkoutPreference.items = [item]
	checkoutPreference.payer = payer
	checkoutPreference.setId("MLA")
	checkoutPreference.setDefaultInstallments(3)
```

[iOS - Objective-C]
```
Item *item = [[Item alloc] initWith_id:@"itemId" title:@"item title" quantity:2 unitPrice:2 description:@"item description" currencyId:@"ARS"];
    
    Payer *payer = [[Payer alloc] initWith_id:@"payerId" email:@"payer@email.com" type:nil identification:nil entityType:nil];
    
    NSArray *items = [NSArray arrayWithObjects:item, item, nil];
        	
PaymentPreference *paymentExclusions = [[PaymentPreference alloc] init];
    paymentExclusions.setDefaultInstallments = 3;
        
    self.pref = [[CheckoutPreference alloc] initWithItems:items payer:payer paymentMethods:paymentExclusions];

	[self.pref setSiteId:@“MLA”];
```

----------

### Preferencia de Flujo

La Preferencia de Flujo permite personalizar y configurar el flujo para que puedas lograr la mejor experiencia de pago. 

En la clase FlowPreference podrás configurar, tanto si deseas mostrar una pantalla con el resumen de lo que se va a pagar (Revisa y Confirma) como si deseas comunicar campañas de descuentos, ¡entre muchas otras opciones!

Para incorporar en el Checkout las opciones configuradas en la clase FlowPreference deberás agregar una instancia de la misma en el inicio del Checkout, como se muestra en el siguiente código:

[Android]

	FlowPreference flowPreference = new FlowPreference.Builder()
                .disableReviewAndConfirmScreen()
                .disableDiscount()
                .disableBankDeals()
                .build();

        new MercadoPagoCheckout.Builder()
                .setActivity(this)
                .setPublicKey(mPublicKey)
                .setCheckoutPreference(mCheckoutPreference())
                .setFlowPreference(flowPreference)
                .startForPayment();

[iOS - Swift]
	
	let flowPrefernece = FlowPreference()
            flowPrefernece.disableReviewAndConfirmScreen()
            flowPrefernece.disableDiscount()
            flowPrefernece.disableBankDeals()

            MercadoPagoCheckout.setFlowPreference(flowPrefernece)

	 let checkout = MercadoPagoCheckout(publicKey: publicKey, accessToken: nil, checkoutPreference: checkoutPreference,
         navigationController: self.navigationController!)
	   checkout.start()
	
[iOS - Objective-C]

	FlowPreference *flowPreference = [[FlowPreference alloc]init];
    [flowPreference disableReviewAndConfirmScreen];
    [flowPreference disableDiscount];
    [flowPreference disableBankDeals];
    [MercadoPagoCheckout setFlowPreference:flowPreference];

	-(void)startMercadoPagoCheckout:(CheckoutPreference *)checkoutPreference {
		    self.mpCheckout = [[MercadoPagoCheckout alloc] initWithPublicKey: TEST_PUBLIC_KEY accessToken: nil checkoutPreference:checkoutPreference paymentData:nil discount:nil navigationController:self.navigationController paymentResult: nil];
    [self.mpCheckout start];
	}

Como se observa en el ejemplo, puedes ocultar el botón de "Promociones" con el método disableBankDeals para aquellos casos en lo que solo solicites pagos en una cuota.

----------

### ¡Limita el tiempo de pago!

El SDK permite añadir un temporizador en el checkout con el fin de limitar el tiempo que el usuario tiene para realizar el pago. Dicho temporizador se configura en la Preferencia de Flujo de la siguiente manera:

[Android]

	CheckoutTimer.FinishListener timerFinishListener = new CheckoutTimer.FinishListener() {
            @Override
            public void onFinish() {
                //Do something
                CheckoutTimer.getInstance().finishCheckout();
            }
        };
        
        FlowPreference flowPreference = new FlowPreference.Builder()
                .setCheckoutTimer(30, timerFinishListener)
                .build();

[iOS - Swift - NO ESTA]
[iOS - Objective-C - NO ESTA]

----------
# Probando la integración

Para probar la integración sigue estos pasos
1. Configura las credenciales de sandbox
2. Crea la preferencia con la public key de sandbox.
3. Completa los datos del formulario, ingresando los dígitos de una tarjeta de prueba. En fecha de expiración debes ingresar cualquier fecha posterior a la actual y en código de seguridad 3 o 4 dígitos dependiendo de la tarjeta.
4. En el nombre del titular de la tarjeta debes ingresar el prefijo correspondiente a lo que quieras probar:
        * **APRO**: Pago aprobado  
        * **CONT**: Pago pendiente  
        * **CALL**: Rechazo llamar para autorizar  
        * **FUND**: Rechazo por monto insuficiente  
        * **SECU**: Rechazo por código de seguridad  
        * **EXPI**: Rechazo por fecha de expiración  
        * **FORM**: Rechazo por error en formulario  
        * **OTHE**: Rechazo general  

### Tajetas para probar nuestro checkout

Usa estas tarjetas de prueba para testear los diferentes resultados del pago.

| País 		| Visa 				 | Mastercard        | American Express |
| ---- 		| ---- 				 | ----------        | ---------------- |
| Argentina  	| 4509 9535 6623 3704|5031 7557 3453 0604|3711 803032 57522 |
| Brasil  	| 4235 6477 2802 5682|5031 4332 1540 6351|3753 651535 56885 |
| Chile   	| 4168 8188 4444 7115|5416 7526 0258 2580|3757 781744 61804 |
| Colombia  	| 4013 5406 8274 6260|5254 1336 7440 3564|3743 781877 55283 |
| México  	| 4075 5957 1648 3764|5474 9254 3267 0366|no disponible     |
| Perú    	| 4009 1753 3280 6176|no disponible      |no disponible     |
| Uruguay  	| 4014 6823 8753 2428|5808 8877 7464 1586|no disponible     |
| Venezuela  	| 4966 3823 3110 9310|5177 0761 6430 0010|no disponible     |

## Prueba rápido con nuestros datos

Usa nuestros datos, así podrás probar rápido la experiencia completa.

| País 		| Public key de prueba 				 | Checkout preference id de prueba        |
| ---- 		| ---- 				 | ----------        |
| Argentina  	| | |
| Brasil  	|  | |
| Chile   	| | |
| Colombia  	| | |
| México  	| | |
| Perú    	| | |
| Uruguay  	| | |
| Venezuela  	| | |
