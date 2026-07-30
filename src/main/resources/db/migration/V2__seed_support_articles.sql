INSERT INTO articles
    (title, slug, summary, content, category, tags, status, views, helpful_count, not_helpful_count, created_at, updated_at)
VALUES
    (
        'Reset a Windows password safely',
        'reset-a-windows-password-safely',
        'Recover access to a Windows workstation without losing local files.',
        '<h2>Before you begin</h2><p>Confirm the employee identity using the approved support procedure. Never ask for the existing password.</p><h2>Reset steps</h2><ol><li>Connect the device to the company network.</li><li>Use the approved password reset portal.</li><li>Lock and unlock Windows with the new password.</li></ol><p>If cached credentials remain, restart the device while connected to the network.</p>',
        'Windows',
        ARRAY['password-reset', 'windows', 'account-access'],
        'PUBLISHED', 148, 31, 2, CURRENT_TIMESTAMP - INTERVAL '80 days', CURRENT_TIMESTAMP - INTERVAL '5 days'
    ),
    (
        'Connect to the corporate VPN',
        'connect-to-the-corporate-vpn',
        'Configure the VPN client and solve the most common connection failures.',
        '<h2>Connect</h2><ol><li>Open the company VPN client.</li><li>Select the default gateway.</li><li>Sign in with your current account.</li></ol><h2>Troubleshooting</h2><p>Check internet access before retrying. If the connection times out, restart the client and verify the system clock.</p>',
        'Network',
        ARRAY['vpn', 'remote-work', 'network'],
        'PUBLISHED', 231, 48, 4, CURRENT_TIMESTAMP - INTERVAL '77 days', CURRENT_TIMESTAMP - INTERVAL '3 days'
    ),
    (
        'Fix VPN authentication failed',
        'fix-vpn-authentication-failed',
        'Resolve rejected VPN credentials, expired passwords, and stale sessions.',
        '<h2>Symptoms</h2><p>The VPN client reports authentication failed even though the username appears correct.</p><h2>Resolution</h2><ul><li>Confirm the password is not expired.</li><li>Remove saved credentials from the client.</li><li>Complete any required multi-factor prompt.</li></ul><p>Escalate after three verified failures to avoid an account lockout.</p>',
        'Network',
        ARRAY['vpn', 'authentication', 'password-reset'],
        'PUBLISHED', 174, 39, 5, CURRENT_TIMESTAMP - INTERVAL '70 days', CURRENT_TIMESTAMP - INTERVAL '8 days'
    ),
    (
        'Troubleshoot slow Wi-Fi',
        'troubleshoot-slow-wi-fi',
        'Identify signal, congestion, and device problems that cause slow wireless access.',
        '<h2>Quick checks</h2><p>Move close to the access point and compare a wired or mobile connection.</p><ul><li>Reconnect to the approved network.</li><li>Disable and enable the wireless adapter.</li><li>Record signal strength and a speed test result.</li></ul><p>Report the location and time if several users are affected.</p>',
        'Network',
        ARRAY['wi-fi', 'performance', 'network'],
        'PUBLISHED', 126, 24, 3, CURRENT_TIMESTAMP - INTERVAL '64 days', CURRENT_TIMESTAMP - INTERVAL '12 days'
    ),
    (
        'Forget and reconnect to a Wi-Fi network',
        'forget-and-reconnect-to-a-wi-fi-network',
        'Remove an outdated wireless profile and establish a clean connection.',
        '<h2>Windows</h2><p>Open Network settings, select Manage known networks, choose the company network, and select Forget.</p><p>Reconnect from the network menu and enter only the credentials requested by the approved sign-in flow.</p>',
        'Network',
        ARRAY['wi-fi', 'windows', 'connection'],
        'PUBLISHED', 94, 21, 1, CURRENT_TIMESTAMP - INTERVAL '61 days', CURRENT_TIMESTAMP - INTERVAL '16 days'
    ),
    (
        'Clear browser cache without deleting passwords',
        'clear-browser-cache-without-deleting-passwords',
        'Refresh cached website files while keeping saved credentials and autofill data.',
        '<h2>Safe cache cleanup</h2><p>Open the browser privacy settings and select cached images and files only. Leave passwords, autofill, and cookies unchecked unless the troubleshooting plan explicitly requires them.</p><p>Close all tabs for the affected site, reopen the browser, and test again.</p>',
        'Browser',
        ARRAY['browser-cache', 'chrome', 'edge'],
        'PUBLISHED', 287, 63, 6, CURRENT_TIMESTAMP - INTERVAL '58 days', CURRENT_TIMESTAMP - INTERVAL '2 days'
    ),
    (
        'Resolve a browser certificate warning',
        'resolve-a-browser-certificate-warning',
        'Check date, network, and certificate details before escalating a TLS warning.',
        '<h2>Do not bypass the warning</h2><p>Confirm the system date and time, then verify that the requested address is correct.</p><p>If the warning affects one internal site, capture the certificate issuer and expiry date. If many sites are affected, check the network proxy and system trust store.</p>',
        'Browser',
        ARRAY['browser', 'certificate', 'tls'],
        'PUBLISHED', 88, 19, 2, CURRENT_TIMESTAMP - INTERVAL '54 days', CURRENT_TIMESTAMP - INTERVAL '10 days'
    ),
    (
        'Install approved software on Windows',
        'install-approved-software-on-windows',
        'Use the managed software catalog and verify that an installation completed.',
        '<h2>Install</h2><ol><li>Open the company software portal.</li><li>Search for the approved application.</li><li>Select Install and wait for the completion message.</li></ol><p>Restart only when the portal requests it. Do not download installers from unapproved websites.</p>',
        'Software',
        ARRAY['software-installation', 'windows', 'self-service'],
        'PUBLISHED', 163, 37, 3, CURRENT_TIMESTAMP - INTERVAL '51 days', CURRENT_TIMESTAMP - INTERVAL '7 days'
    ),
    (
        'Repair a failed software installation',
        'repair-a-failed-software-installation',
        'Collect useful evidence and recover from a managed installation failure.',
        '<h2>Collect details</h2><p>Record the application name, error code, time, and available disk space.</p><h2>Retry safely</h2><p>Restart the software portal, run its sync action, and retry once. Repeated attempts can hide the original failure, so attach the portal log when escalating.</p>',
        'Software',
        ARRAY['software-installation', 'troubleshooting', 'logs'],
        'PUBLISHED', 102, 18, 4, CURRENT_TIMESTAMP - INTERVAL '47 days', CURRENT_TIMESTAMP - INTERVAL '14 days'
    ),
    (
        'Add a network printer in Windows',
        'add-a-network-printer-in-windows',
        'Connect to an approved office printer using its published queue.',
        '<h2>Add the printer</h2><p>Open Printers and scanners, select Add device, and choose the published queue that matches the printer label.</p><p>Print a test page. Avoid installing drivers from a vendor website unless the support catalog directs you to do so.</p>',
        'Printing',
        ARRAY['printer', 'windows', 'network'],
        'PUBLISHED', 119, 27, 2, CURRENT_TIMESTAMP - INTERVAL '44 days', CURRENT_TIMESTAMP - INTERVAL '9 days'
    ),
    (
        'Clear a stuck print queue',
        'clear-a-stuck-print-queue',
        'Remove blocked jobs and restore normal printing without restarting the printer first.',
        '<h2>Clear the queue</h2><ol><li>Open the printer queue.</li><li>Cancel the oldest failed job.</li><li>Pause and resume the queue.</li></ol><p>If jobs remain, restart the Print Spooler service with approved administrative access and send one test page.</p>',
        'Printing',
        ARRAY['printer', 'print-queue', 'windows'],
        'PUBLISHED', 141, 30, 3, CURRENT_TIMESTAMP - INTERVAL '40 days', CURRENT_TIMESTAMP - INTERVAL '6 days'
    ),
    (
        'Printer shows offline',
        'printer-shows-offline',
        'Determine whether an offline printer problem is local, network-wide, or hardware-related.',
        '<h2>Checks</h2><ul><li>Confirm the printer is powered on and has no paper or toner alert.</li><li>Check whether another employee can print.</li><li>Verify the selected queue is the approved network queue.</li></ul><p>Report the printer label and its display message when escalating.</p>',
        'Printing',
        ARRAY['printer', 'offline', 'network'],
        'PUBLISHED', 156, 34, 4, CURRENT_TIMESTAMP - INTERVAL '36 days', CURRENT_TIMESTAMP - INTERVAL '4 days'
    ),
    (
        'Configure email on a replacement laptop',
        'configure-email-on-a-replacement-laptop',
        'Sign in to the supported mail client and confirm synchronization.',
        '<h2>Set up</h2><p>Open the managed mail client and sign in with the company address. Complete the approved authentication prompt.</p><p>Wait for folders and calendar data to synchronize, then send a message to your own address as a test.</p>',
        'Email',
        ARRAY['email', 'outlook', 'replacement-device'],
        'PUBLISHED', 132, 28, 2, CURRENT_TIMESTAMP - INTERVAL '33 days', CURRENT_TIMESTAMP - INTERVAL '11 days'
    ),
    (
        'Fix email stuck in the outbox',
        'fix-email-stuck-in-the-outbox',
        'Identify large attachments, offline mode, and synchronization failures.',
        '<h2>Resolution</h2><p>Confirm the client is online. Open the outbox and inspect the oldest message for a large attachment.</p><p>Move the message to Drafts, reduce the attachment size or share an approved link, then send it again.</p>',
        'Email',
        ARRAY['email', 'outbox', 'attachments'],
        'PUBLISHED', 177, 42, 5, CURRENT_TIMESTAMP - INTERVAL '29 days', CURRENT_TIMESTAMP - INTERVAL '1 day'
    ),
    (
        'Recover missing email search results',
        'recover-missing-email-search-results',
        'Use focused search terms and rebuild the local search index when necessary.',
        '<h2>Search checks</h2><p>Remove unnecessary filters and search by sender plus a short subject phrase. Verify that the correct mailbox and date range are selected.</p><p>If webmail finds the message but the desktop client does not, rebuild the local search index using the approved support procedure.</p>',
        'Email',
        ARRAY['email', 'search', 'indexing'],
        'PUBLISHED', 83, 16, 3, CURRENT_TIMESTAMP - INTERVAL '25 days', CURRENT_TIMESTAMP - INTERVAL '13 days'
    ),
    (
        'Free disk space on Windows',
        'free-disk-space-on-windows',
        'Recover space with built-in tools while protecting business files.',
        '<h2>Safe cleanup</h2><p>Use Windows Storage settings to remove temporary files and empty the recycle bin. Review Downloads manually before deleting anything.</p><p>Move business documents only to approved storage. Do not use third-party cleanup utilities.</p>',
        'Windows',
        ARRAY['windows', 'disk-space', 'maintenance'],
        'PUBLISHED', 205, 51, 3, CURRENT_TIMESTAMP - INTERVAL '22 days', CURRENT_TIMESTAMP - INTERVAL '2 days'
    ),
    (
        'Collect Windows system information',
        'collect-windows-system-information',
        'Capture the device details support needs without collecting document contents.',
        '<h2>Collect details</h2><p>Open System Information and record the Windows edition, version, system model, installed memory, and BIOS version.</p><p>Do not include product keys, personal documents, browser history, or saved credentials.</p>',
        'Windows',
        ARRAY['windows', 'diagnostics', 'privacy'],
        'PUBLISHED', 76, 17, 1, CURRENT_TIMESTAMP - INTERVAL '18 days', CURRENT_TIMESTAMP - INTERVAL '8 days'
    ),
    (
        'Fix a frozen Windows application',
        'fix-a-frozen-windows-application',
        'Recover an unresponsive application and preserve useful troubleshooting evidence.',
        '<h2>Recover</h2><p>Wait one minute for background work to finish. If the application remains unresponsive, capture the application name and visible error, then close it from Task Manager.</p><p>Reopen it once. If the problem repeats, collect the relevant event log and escalate.</p>',
        'Windows',
        ARRAY['windows', 'application', 'performance'],
        'PUBLISHED', 111, 25, 2, CURRENT_TIMESTAMP - INTERVAL '14 days', CURRENT_TIMESTAMP - INTERVAL '3 days'
    ),
    (
        'Check whether a support link is safe',
        'check-whether-a-support-link-is-safe',
        'Validate a support URL before opening it or entering company credentials.',
        '<h2>Review the link</h2><p>Read the full hostname, check for misspellings, and confirm that it uses HTTPS. Compare the destination with the official support portal bookmark.</p><p>Never approve an unexpected sign-in or multi-factor prompt. Report suspicious messages through the approved channel.</p>',
        'Security',
        ARRAY['browser', 'phishing', 'security'],
        'PUBLISHED', 193, 58, 2, CURRENT_TIMESTAMP - INTERVAL '10 days', CURRENT_TIMESTAMP - INTERVAL '1 day'
    ),
    (
        'Prepare a device for remote support',
        'prepare-a-device-for-remote-support',
        'Draft procedure for gathering consent and starting an approved remote session.',
        '<h2>Draft procedure</h2><p>Confirm the ticket number, explain what the technician will be able to see, and obtain consent before starting the approved remote support tool.</p>',
        'Remote Support',
        ARRAY['remote-support', 'privacy', 'draft'],
        'DRAFT', 0, 0, 0, CURRENT_TIMESTAMP - INTERVAL '2 days', CURRENT_TIMESTAMP - INTERVAL '2 days'
    );

